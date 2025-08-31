# ---- Build stage ----
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Install sbt via modern repo key method (no apt-key)
RUN apt-get update && apt-get install -y curl gnupg2 ca-certificates && \
    mkdir -p /usr/share/keyrings && \
    curl -fsSL https://repo.scala-sbt.org/scalasbt/debian/scalasbt-release.asc | gpg --dearmor > /usr/share/keyrings/sbt-release.gpg && \
    echo "deb [signed-by=/usr/share/keyrings/sbt-release.gpg] https://repo.scala-sbt.org/scalasbt/debian all main" > /etc/apt/sources.list.d/sbt.list && \
    apt-get update && apt-get install -y sbt && \
    rm -rf /var/lib/apt/lists/*

# Cache sbt deps
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

# Replace with your actual script name from target/universal/stage/bin
CMD ["./stage/bin/<APP_SCRIPT_NAME>","-Dplay.http.secret.key=${PLAY_SECRET}","-Dplay.server.http.port=${PORT}","-Dconfig.resource=application.conf"]
