FROM openjdk:11
ENV APP_HOME=/usr/app
WORKDIR $APP_HOME
COPY target/*.jar Job-Portal.jar
EXPOSE 8080
CMD ["java", "-jar", "Job-Portal.jar"]