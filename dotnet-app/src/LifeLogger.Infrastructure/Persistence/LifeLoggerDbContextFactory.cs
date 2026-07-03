using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Design;

namespace LifeLogger.Infrastructure.Persistence;

public sealed class LifeLoggerDbContextFactory : IDesignTimeDbContextFactory<LifeLoggerDbContext>
{
    public LifeLoggerDbContext CreateDbContext(string[] args)
    {
        var optionsBuilder = new DbContextOptionsBuilder<LifeLoggerDbContext>();
        optionsBuilder.UseNpgsql(
            "Host=localhost;Port=5432;Database=lifelogger;Username=postgres;Password=postgres");

        return new LifeLoggerDbContext(optionsBuilder.Options);
    }
}
