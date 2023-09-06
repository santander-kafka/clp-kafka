FROM openjdk:17-alpine3.14
RUN apk upgrade --no-cache && apk add --no-cache postgresql-client bash openssl libgcc libstdc++ ncurses-libs
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]