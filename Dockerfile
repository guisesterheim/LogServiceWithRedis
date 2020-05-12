FROM adoptopenjdk/openjdk11

VOLUME /tmp

ADD target/LogAnalyticsService-*.jar application.jar

RUN bash -c 'touch /application.jar'

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "-Xms512m", "-Xmx512m", "-Dspring.profiles.active=prod", "/application.jar"]