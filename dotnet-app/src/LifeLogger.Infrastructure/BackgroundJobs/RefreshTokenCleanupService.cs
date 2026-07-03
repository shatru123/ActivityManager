using LifeLogger.Infrastructure.Persistence;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;

namespace LifeLogger.Infrastructure.BackgroundJobs;

public sealed class RefreshTokenCleanupService(
    IServiceScopeFactory scopeFactory,
    ILogger<RefreshTokenCleanupService> logger) : BackgroundService
{
    private readonly IServiceScopeFactory _scopeFactory = scopeFactory;
    private readonly ILogger<RefreshTokenCleanupService> _logger = logger;

    protected override async Task ExecuteAsync(CancellationToken stoppingToken)
    {
        while (!stoppingToken.IsCancellationRequested)
        {
            try
            {
                using var scope = _scopeFactory.CreateScope();
                var dbContext = scope.ServiceProvider.GetRequiredService<LifeLoggerDbContext>();
                var cutoff = DateTimeOffset.UtcNow.AddDays(-7);

                var expiredTokens = await dbContext.RefreshTokens
                    .Where(token => token.ExpiresAtUtc < cutoff || token.RevokedAtUtc < cutoff)
                    .ToListAsync(stoppingToken);

                if (expiredTokens.Count > 0)
                {
                    dbContext.RefreshTokens.RemoveRange(expiredTokens);
                    await dbContext.SaveChangesAsync(stoppingToken);
                }
            }
            catch (Exception exception)
            {
                _logger.LogError(exception, "Failed to prune refresh tokens.");
            }

            await Task.Delay(TimeSpan.FromHours(12), stoppingToken);
        }
    }
}
