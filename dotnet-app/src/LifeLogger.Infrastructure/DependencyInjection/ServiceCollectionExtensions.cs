using LifeLogger.Application.Abstractions.Authentication;
using LifeLogger.Domain.Entities;
using LifeLogger.Infrastructure.Authentication;
using LifeLogger.Infrastructure.BackgroundJobs;
using LifeLogger.Infrastructure.Configuration;
using LifeLogger.Infrastructure.Persistence;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;

namespace LifeLogger.Infrastructure.DependencyInjection;

public static class ServiceCollectionExtensions
{
    public static IServiceCollection AddInfrastructureServices(
        this IServiceCollection services,
        IConfiguration configuration)
    {
        var connectionString = configuration.GetConnectionString("Database")
            ?? throw new InvalidOperationException("Connection string 'Database' was not found.");

        services.Configure<JwtOptions>(configuration.GetSection(JwtOptions.SectionName));
        services.AddSingleton(TimeProvider.System);

        services.AddDbContext<LifeLoggerDbContext>(options =>
            options.UseNpgsql(connectionString));

        services.AddScoped<IPasswordHasher<User>, PasswordHasher<User>>();
        services.AddScoped<IJwtTokenGenerator, JwtTokenGenerator>();
        services.AddScoped<IRefreshTokenProtector, RefreshTokenProtector>();
        services.AddScoped<IAuthService, AuthService>();

        services.AddHostedService<RefreshTokenCleanupService>();

        return services;
    }
}
