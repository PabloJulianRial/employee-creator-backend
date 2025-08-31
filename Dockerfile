# ---- Build stage ----
FROM hseeberger/scala-sbt:17.0.11_1.10.2_2.13.14 AS build
WORKDIR /app

# cache sbt deps
COPY project ./project
COPY build.sbt ./
RUN sbt update

# build
COPY . .
RUN sbt stage

# ---- Run stage ----
FROM eclipse-temurin:17-jre
WORKDIR /opt/app
COPY --from=build /app/target/universal/stage ./stage

# Render injects $PORT; pass it to Play
ENV PORT=10000
EXPOSE 10000

# GC opts help on small instances
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

CMD ["./stage/bin/<APP_SCRIPT_NAME>",
     "-J-XX:+UseContainerSupport",
     "-J-XX:MaxRAMPercentage=75.0",
     "-Dplay.http.secret.key=${PLAY_SECRET}",
     "-Dplay.server.http.port=${PORT}",
     "-Dconfig.resource=application.conf"]
