using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
using LifeLogger.Application.Abstractions.Authentication;
using LifeLogger.Domain.Entities;
using LifeLogger.Infrastructure.Configuration;
using Microsoft.Extensions.Options;
using Microsoft.IdentityModel.Tokens;

namespace LifeLogger.Infrastructure.Authentication;

public sealed class JwtTokenGenerator(
    IOptions<JwtOptions> jwtOptions,
    TimeProvider timeProvider) : IJwtTokenGenerator
{
    private readonly JwtOptions _jwtOptions = jwtOptions.Value;
    private readonly TimeProvider _timeProvider = timeProvider;

    public (string Token, DateTimeOffset ExpiresAtUtc) Generate(User user)
    {
        var now = _timeProvider.GetUtcNow();
        var expiresAt = now.AddMinutes(_jwtOptions.AccessTokenLifetimeMinutes);
        var securityKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_jwtOptions.SigningKey));
        var credentials = new SigningCredentials(securityKey, SecurityAlgorithms.HmacSha256);

        var claims = new[]
        {
            new Claim(JwtRegisteredClaimNames.Sub, user.Id.ToString()),
            new Claim(JwtRegisteredClaimNames.Email, user.Email),
            new Claim(JwtRegisteredClaimNames.Name, user.FullName),
            new Claim(JwtRegisteredClaimNames.Jti, Guid.NewGuid().ToString()),
            new Claim(ClaimTypes.NameIdentifier, user.Id.ToString()),
            new Claim(ClaimTypes.Name, user.FullName)
        };

        var tokenDescriptor = new JwtSecurityToken(
            issuer: _jwtOptions.Issuer,
            audience: _jwtOptions.Audience,
            claims: claims,
            notBefore: now.UtcDateTime,
            expires: expiresAt.UtcDateTime,
            signingCredentials: credentials);

        var token = new JwtSecurityTokenHandler().WriteToken(tokenDescriptor);
        return (token, expiresAt);
    }
}
