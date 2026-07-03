using LifeLogger.Application.Contracts.Authentication;

namespace LifeLogger.Application.Abstractions.Authentication;

public interface IAuthService
{
    Task<AuthenticationResult> RegisterAsync(
        RegisterRequest request,
        string? ipAddress,
        string? userAgent,
        CancellationToken cancellationToken);

    Task<AuthenticationResult> LoginAsync(
        LoginRequest request,
        string? ipAddress,
        string? userAgent,
        CancellationToken cancellationToken);

    Task<AuthenticationResult> RefreshAsync(
        RefreshTokenRequest request,
        string? ipAddress,
        string? userAgent,
        CancellationToken cancellationToken);
}
