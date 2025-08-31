# ---- Build stage ----
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Minimal tools
RUN apt-get update && apt-get install -y curl git && rm -rf /var/lib/apt/lists/*

# Install sbt-extras launcher (no apt repo keys)
RUN curl -fsSL https://raw.githubusercontent.com/dwijnand/sbt-extras/master/sbt -o /usr/local/bin/sbt \
 && chmod +x /usr/local/bin/sbt

# Prime dependency cache
COPY project ./project
COPY build.sbt ./
RUN sbt -batch update

# Build the app
COPY . .
RUN sbt -batch stage

# ---- Run stage ----
FROM eclipse-temurin:17-jre
WORKDIR /opt/app
COPY --from=build /app/target/universal/stage ./stage

ENV PORT=10000
EXPOSE 10000
ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# IMPORTANT: replace with the exact script name from target/universal/stage/bin
CMD ["./stage/bin/employer-creator-backend","-Dplay.http.secret.key=${PLAY_SECRET}","-Dplay.server.http.port=${PORT}","-Dconfig.resource=application.conf"]
