using System.Security.Cryptography;
using LifeLogger.Application.Abstractions.Authentication;

namespace LifeLogger.Infrastructure.Authentication;

public sealed class RefreshTokenProtector : IRefreshTokenProtector
{
    public string GenerateToken()
    {
        return Convert.ToBase64String(RandomNumberGenerator.GetBytes(64));
    }

    public string HashToken(string token)
    {
        var bytes = SHA256.HashData(System.Text.Encoding.UTF8.GetBytes(token));
        return Convert.ToHexString(bytes);
    }
}
