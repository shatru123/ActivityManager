using LifeLogger.Application.Abstractions.Authentication;
using LifeLogger.Application.Contracts.Authentication;
using Microsoft.AspNetCore.Mvc;

namespace LifeLogger.Api.Endpoints;

public static class AuthEndpoints
{
    public static IEndpointRouteBuilder MapAuthEndpoints(this IEndpointRouteBuilder endpoints)
    {
        var group = endpoints.MapGroup("/api/auth")
            .WithTags("Authentication");

        group.MapPost("/register", async Task<IResult> (
            RegisterRequest request,
            HttpContext httpContext,
            IAuthService authService,
            CancellationToken cancellationToken) =>
        {
            var result = await authService.RegisterAsync(
                request,
                httpContext.Connection.RemoteIpAddress?.ToString(),
                httpContext.Request.Headers.UserAgent.ToString(),
                cancellationToken);

            return result.IsSuccess
                ? TypedResults.Ok(result.Response)
                : TypedResults.Problem(result.Error, statusCode: result.StatusCode);
        })
        .WithName("Register");

        group.MapPost("/login", async Task<IResult> (
            LoginRequest request,
            HttpContext httpContext,
            IAuthService authService,
            CancellationToken cancellationToken) =>
        {
            var result = await authService.LoginAsync(
                request,
                httpContext.Connection.RemoteIpAddress?.ToString(),
                httpContext.Request.Headers.UserAgent.ToString(),
                cancellationToken);

            return result.IsSuccess
                ? TypedResults.Ok(result.Response)
                : TypedResults.Problem(result.Error, statusCode: result.StatusCode);
        })
        .WithName("Login");

        group.MapPost("/refresh", async Task<IResult> (
            RefreshTokenRequest request,
            HttpContext httpContext,
            IAuthService authService,
            CancellationToken cancellationToken) =>
        {
            var result = await authService.RefreshAsync(
                request,
                httpContext.Connection.RemoteIpAddress?.ToString(),
                httpContext.Request.Headers.UserAgent.ToString(),
                cancellationToken);

            return result.IsSuccess
                ? TypedResults.Ok(result.Response)
                : TypedResults.Problem(result.Error, statusCode: result.StatusCode);
        })
        .WithName("RefreshToken");

        return endpoints;
    }
}
