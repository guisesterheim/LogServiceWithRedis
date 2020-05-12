FROM adoptopenjdk/openjdk11

RUN mkdir /app

WORKDIR /app

COPY build/libs/log-service-*.jar /app/application.jar

EXPOSE 8080

CMD ["java", "-jar", "-Xms512m", "-Xmx512m", "-Dspring.profiles.active=dev", "/app/application.jar"]