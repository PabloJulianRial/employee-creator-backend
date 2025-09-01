FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

RUN apt-get update && apt-get install -y curl git && rm -rf /var/lib/apt/lists/*

RUN curl -fsSL https://raw.githubusercontent.com/dwijnand/sbt-extras/master/sbt -o /usr/local/bin/sbt \
 && chmod +x /usr/local/bin/sbt

COPY project ./project
COPY build.sbt ./
RUN sbt -batch update

COPY . .
RUN sbt -batch stage

FROM eclipse-temurin:17-jre
WORKDIR /opt/app
COPY --from=build /app/target/universal/stage ./stage

EXPOSE 10000

ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

CMD sh -lc './stage/bin/employee-creator-backend \
  -Dhttp.port="$PORT" \
  -Dplay.http.secret.key="$PLAY_SECRET" \
  -Dconfig.resource=application.conf'
