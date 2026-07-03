using LifeLogger.Domain.Common;

namespace LifeLogger.Domain.Entities;

public sealed class RefreshToken : AuditableEntity
{
    public Guid UserId { get; set; }

    public User User { get; set; } = null!;

    public string TokenHash { get; set; } = string.Empty;

    public DateTimeOffset ExpiresAtUtc { get; set; }

    public DateTimeOffset? RevokedAtUtc { get; set; }

    public string? CreatedByIp { get; set; }

    public string? UserAgent { get; set; }

    public string? ReplacedByTokenHash { get; set; }

    public bool IsActive => RevokedAtUtc is null && ExpiresAtUtc > DateTimeOffset.UtcNow;
}
