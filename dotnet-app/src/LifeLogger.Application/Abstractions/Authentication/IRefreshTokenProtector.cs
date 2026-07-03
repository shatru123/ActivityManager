namespace LifeLogger.Application.Abstractions.Authentication;

public interface IRefreshTokenProtector
{
    string GenerateToken();

    string HashToken(string token);
}
