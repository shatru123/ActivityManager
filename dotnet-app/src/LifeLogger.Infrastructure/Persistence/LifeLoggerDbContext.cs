using LifeLogger.Domain.Entities;
using Microsoft.EntityFrameworkCore;

namespace LifeLogger.Infrastructure.Persistence;

public sealed class LifeLoggerDbContext(DbContextOptions<LifeLoggerDbContext> options) : DbContext(options)
{
    public DbSet<User> Users => Set<User>();

    public DbSet<RefreshToken> RefreshTokens => Set<RefreshToken>();

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.ApplyConfigurationsFromAssembly(typeof(LifeLoggerDbContext).Assembly);
    }

    public override Task<int> SaveChangesAsync(CancellationToken cancellationToken = default)
    {
        var entries = ChangeTracker
            .Entries()
            .Where(entry => entry.Entity is Domain.Common.AuditableEntity
                && (entry.State is EntityState.Added or EntityState.Modified));

        foreach (var entry in entries)
        {
            var entity = (Domain.Common.AuditableEntity)entry.Entity;
            entity.UpdatedAtUtc = DateTimeOffset.UtcNow;

            if (entry.State == EntityState.Added)
            {
                entity.CreatedAtUtc = DateTimeOffset.UtcNow;
            }
        }

        return base.SaveChangesAsync(cancellationToken);
    }
}
