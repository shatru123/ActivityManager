namespace LifeLogger.Application.Contracts.Authentication;

public sealed record AuthResponse(
    Guid UserId,
    string FullName,
    string Email,
    string AccessToken,
    string RefreshToken,
    DateTimeOffset AccessTokenExpiresAtUtc);
