using LifeLogger.Application.Abstractions.Authentication;
using LifeLogger.Application.Contracts.Authentication;
using LifeLogger.Domain.Entities;
using LifeLogger.Infrastructure.Configuration;
using LifeLogger.Infrastructure.Persistence;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Options;

namespace LifeLogger.Infrastructure.Authentication;

public sealed class AuthService(
    LifeLoggerDbContext dbContext,
    IPasswordHasher<User> passwordHasher,
    IJwtTokenGenerator jwtTokenGenerator,
    IRefreshTokenProtector refreshTokenProtector,
    IOptions<JwtOptions> jwtOptions,
    TimeProvider timeProvider) : IAuthService
{
    private readonly LifeLoggerDbContext _dbContext = dbContext;
    private readonly IPasswordHasher<User> _passwordHasher = passwordHasher;
    private readonly IJwtTokenGenerator _jwtTokenGenerator = jwtTokenGenerator;
    private readonly IRefreshTokenProtector _refreshTokenProtector = refreshTokenProtector;
    private readonly JwtOptions _jwtOptions = jwtOptions.Value;
    private readonly TimeProvider _timeProvider = timeProvider;

    public async Task<AuthenticationResult> RegisterAsync(
        RegisterRequest request,
        string? ipAddress,
        string? userAgent,
        CancellationToken cancellationToken)
    {
        if (string.IsNullOrWhiteSpace(request.FullName) ||
            string.IsNullOrWhiteSpace(request.Email) ||
            string.IsNullOrWhiteSpace(request.Password))
        {
            return AuthenticationResult.Failure("Full name, email, and password are required.", 400);
        }

        var normalizedEmail = request.Email.Trim().ToLowerInvariant();
        var emailExists = await _dbContext.Users
            .AnyAsync(user => user.Email == normalizedEmail, cancellationToken);

        if (emailExists)
        {
            return AuthenticationResult.Failure("An account with this email already exists.", 409);
        }

        var user = new User
        {
            FullName = request.FullName.Trim(),
            Email = normalizedEmail
        };
        user.PasswordHash = _passwordHasher.HashPassword(user, request.Password);

        _dbContext.Users.Add(user);

        var response = await IssueTokensAsync(user, ipAddress, userAgent, cancellationToken);
        return AuthenticationResult.Success(response);
    }

    public async Task<AuthenticationResult> LoginAsync(
        LoginRequest request,
        string? ipAddress,
        string? userAgent,
        CancellationToken cancellationToken)
    {
        if (string.IsNullOrWhiteSpace(request.Email) || string.IsNullOrWhiteSpace(request.Password))
        {
            return AuthenticationResult.Failure("Email and password are required.", 400);
        }

        var normalizedEmail = request.Email.Trim().ToLowerInvariant();
        var user = await _dbContext.Users
            .FirstOrDefaultAsync(candidate => candidate.Email == normalizedEmail, cancellationToken);

        if (user is null)
        {
            return AuthenticationResult.Failure("Invalid email or password.", 401);
        }

        var verification = _passwordHasher.VerifyHashedPassword(user, user.PasswordHash, request.Password);
        if (verification == PasswordVerificationResult.Failed)
        {
            return AuthenticationResult.Failure("Invalid email or password.", 401);
        }

        var response = await IssueTokensAsync(user, ipAddress, userAgent, cancellationToken);
        return AuthenticationResult.Success(response);
    }

    public async Task<AuthenticationResult> RefreshAsync(
        RefreshTokenRequest request,
        string? ipAddress,
        string? userAgent,
        CancellationToken cancellationToken)
    {
        if (string.IsNullOrWhiteSpace(request.RefreshToken))
        {
            return AuthenticationResult.Failure("Refresh token is required.", 400);
        }

        var refreshTokenHash = _refreshTokenProtector.HashToken(request.RefreshToken);
        var existingToken = await _dbContext.RefreshTokens
            .Include(token => token.User)
            .FirstOrDefaultAsync(token => token.TokenHash == refreshTokenHash, cancellationToken);

        if (existingToken is null || !existingToken.IsActive)
        {
            return AuthenticationResult.Failure("Refresh token is invalid or expired.", 401);
        }

        existingToken.RevokedAtUtc = _timeProvider.GetUtcNow();

        var replacementToken = _refreshTokenProtector.GenerateToken();
        existingToken.ReplacedByTokenHash = _refreshTokenProtector.HashToken(replacementToken);

        var accessToken = _jwtTokenGenerator.Generate(existingToken.User);
        var refreshToken = new RefreshToken
        {
            UserId = existingToken.UserId,
            TokenHash = existingToken.ReplacedByTokenHash,
            ExpiresAtUtc = _timeProvider.GetUtcNow().AddDays(_jwtOptions.RefreshTokenLifetimeDays),
            CreatedByIp = ipAddress,
            UserAgent = userAgent
        };

        _dbContext.RefreshTokens.Add(refreshToken);
        await _dbContext.SaveChangesAsync(cancellationToken);

        return AuthenticationResult.Success(new AuthResponse(
            existingToken.User.Id,
            existingToken.User.FullName,
            existingToken.User.Email,
            accessToken.Token,
            replacementToken,
            accessToken.ExpiresAtUtc));
    }

    private async Task<AuthResponse> IssueTokensAsync(
        User user,
        string? ipAddress,
        string? userAgent,
        CancellationToken cancellationToken)
    {
        var accessToken = _jwtTokenGenerator.Generate(user);
        var rawRefreshToken = _refreshTokenProtector.GenerateToken();

        var refreshToken = new RefreshToken
        {
            User = user,
            TokenHash = _refreshTokenProtector.HashToken(rawRefreshToken),
            ExpiresAtUtc = _timeProvider.GetUtcNow().AddDays(_jwtOptions.RefreshTokenLifetimeDays),
            CreatedByIp = ipAddress,
            UserAgent = userAgent
        };

        _dbContext.RefreshTokens.Add(refreshToken);
        await _dbContext.SaveChangesAsync(cancellationToken);

        return new AuthResponse(
            user.Id,
            user.FullName,
            user.Email,
            accessToken.Token,
            rawRefreshToken,
            accessToken.ExpiresAtUtc);
    }
}
