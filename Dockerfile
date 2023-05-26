FROM openjdk:17
ADD /build/libs/expense-tracker-0.0.1-SNAPSHOT.jar expense-tracker-0.0.1-SNAPSHOT.jar
EXPOSE 8082
ENTRYPOINT ["java", "-Xdebug", "-jar", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5006", "expense-tracker-0.0.1-SNAPSHOT.jar"]