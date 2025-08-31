FROM hseeberger/scala-sbt:17.0.11_1.10.2_2.13.14 AS build
WORKDIR /app

COPY project ./project
COPY build.sbt ./
RUN sbt update

COPY . .
RUN sbt stage

FROM eclipse-temurin:17-jre
WORKDIR /opt/app
COPY --from=build /app/target/universal/stage ./stage

ENV PORT=10000
EXPOSE 10000

ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

CMD ["./stage/bin/employer-creator-backend","-Dplay.http.secret.key=${PLAY_SECRET}","-Dplay.server.http.port=${PORT}","-Dconfig.resource=application.conf"]
