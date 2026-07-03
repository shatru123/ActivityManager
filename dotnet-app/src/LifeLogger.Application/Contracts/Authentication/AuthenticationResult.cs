namespace LifeLogger.Application.Contracts.Authentication;

public sealed record AuthenticationResult(
    bool IsSuccess,
    AuthResponse? Response,
    string? Error,
    int StatusCode)
{
    public static AuthenticationResult Success(AuthResponse response) =>
        new(true, response, null, 200);

    public static AuthenticationResult Failure(string error, int statusCode) =>
        new(false, null, error, statusCode);
}
