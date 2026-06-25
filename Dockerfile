# ===================================================================
# ClimaLens :: Multi-Stage Dockerfile
# ===================================================================
# Build stage:  Maven + JDK 21  →  compile & package
# Runtime stage: JRE 21 (distroless)  →  run the fat JAR
# ===================================================================

# ---------- Build Stage ----------
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

WORKDIR /build

# Copy root POM and module POMs first (leverage Docker layer caching)
COPY pom.xml ./
COPY common/pom.xml ./common/
COPY ingestion/pom.xml ./ingestion/
COPY scoring/pom.xml ./scoring/
COPY api/pom.xml ./api/

# Download dependencies (cached unless POMs change)
RUN mvn dependency:go-offline -q -B

# Copy source code
COPY common/src ./common/src
COPY ingestion/src ./ingestion/src
COPY scoring/src ./scoring/src
COPY api/src ./api/src

# Build the fat JAR (skip tests entirely — compile + test phases; run tests in separate step)
RUN mvn package -q -B -Dmaven.test.skip=true -pl api -am

# ---------- Runtime Stage ----------
FROM eclipse-temurin:21-jre-alpine AS runtime

# Install curl for health checks
RUN apk add --no-cache curl

# Create a non-root user
RUN addgroup -S climalens && adduser -S climalens -G climalens
USER climalens

WORKDIR /app

# Copy the fat JAR from the builder stage
COPY --from=builder /build/api/target/*.jar ./climalens.jar

# Expose the application port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -sf http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", \
    "-XX:+UseZGC", \
    "-XX:MaxRAMPercentage=75.0", \
    "-jar", \
    "climalens.jar"]