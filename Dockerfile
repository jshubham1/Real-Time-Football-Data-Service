FROM maven:3.5-jdk-8 AS build  
COPY src /usr/src/app/src  
COPY pom.xml /usr/src/app  
RUN mvn -DskipTests=true -f /usr/src/app/pom.xml clean package 

FROM gcr.io/distroless/java  
COPY --from=build /usr/src/app/target/real-time-football-service-1.0.jar /usr/app/real-time-football-service-1.0.jar
EXPOSE 8080  
ENTRYPOINT ["java","-jar","/usr/app/real-time-football-service-1.0.jar"]
