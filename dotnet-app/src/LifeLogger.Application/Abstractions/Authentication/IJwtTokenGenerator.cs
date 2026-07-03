using LifeLogger.Domain.Entities;

namespace LifeLogger.Application.Abstractions.Authentication;

public interface IJwtTokenGenerator
{
    (string Token, DateTimeOffset ExpiresAtUtc) Generate(User user);
}
