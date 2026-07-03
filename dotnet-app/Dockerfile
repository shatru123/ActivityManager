FROM mcr.microsoft.com/dotnet/sdk:8.0 AS build
WORKDIR /src

COPY LifeLogger.sln ./
COPY src/LifeLogger.Api/LifeLogger.Api.csproj src/LifeLogger.Api/
COPY src/LifeLogger.Application/LifeLogger.Application.csproj src/LifeLogger.Application/
COPY src/LifeLogger.Domain/LifeLogger.Domain.csproj src/LifeLogger.Domain/
COPY src/LifeLogger.Infrastructure/LifeLogger.Infrastructure.csproj src/LifeLogger.Infrastructure/
RUN dotnet restore src/LifeLogger.Api/LifeLogger.Api.csproj

COPY . .
RUN dotnet publish src/LifeLogger.Api/LifeLogger.Api.csproj -c Release -o /app/publish /p:UseAppHost=false

FROM mcr.microsoft.com/dotnet/aspnet:8.0 AS final
WORKDIR /app
ENV ASPNETCORE_URLS=http://+:8080
EXPOSE 8080

COPY --from=build /app/publish .
ENTRYPOINT ["dotnet", "LifeLogger.Api.dll"]
