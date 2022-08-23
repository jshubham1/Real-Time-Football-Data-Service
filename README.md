# Real-Time-Football-Data-Service
- [`Overview`](#Overview)
- [`Setup Instructions`](#Setup-Instructions)
- [`Docker Documentation`](#Docker-Documentation)
- [`Swagger UI`](#Swagger-UI)
- [`Flowchart`](#Flowchart)
- [`API Links`](#API-Links)

## Overview
Gets the list of all the standing for a given league id, country name and team name.

I used following tech-stack to build this application. 

1. Java 8.
2. Spring boot v2.7.2.

In addition to the above technologies I have used the following libraries - 

1. **Feign Client** - Feign is a declarative web service client. It makes writing web service clients easier.
2. **Controller Advice** - To handle both Runtime and custom application the exceptions in the application
3. **HATEOS** - Hypermedia as the Engine of Application State (HATEOAS) is a constraint of the REST application architecture that distinguishes it from other network application architectures. With HATEOAS, a client interacts with a network application whose application servers provide information dynamically through hypermedia.
4. **Open API** - The OpenAPI Specification, previously known as the Swagger Specification, is a specification for machine-readable interface files for describing, producing, consuming, and visualizing RESTful web services.
5. **Spring Actuators** - Monitoring our app, gathering metrics, understanding traffic, or the state of our database become trivial with this dependency.
6. **Lombok** - I used lombok to avoid writing boilerplate code such as constructors, getter-setter etc.


## Swagger-UI
![Alt text](swagger-ui.png?raw=true "Swagger Home Page")

## Flowchart
![Alt text](flowchart.svg?raw=true "Flowchart")


## Setup Instructions

Steps to run this application - 

1. Clone this application in you system.
2. Import it in the intellij as maven project.
3. Let it get indexed. 
4. Run the application and check the swagger-ui page.

## Docker Documentation

## API Links
We can deploy this application in various environments such as dev, test, qa and Production

| Actuator API / Swagger     | Local | Development | QA | Release | Production |
| ------------ | ----------- | ----------- | ---| --- | ----|
| Health|http://localhost:8080/football-service/_manage/health|https://dev.football.standing.com/football-service/_manage/health| https://test.football.standing.com/football-service/_manage/health| https://stage.football.standing.com/football-service/_manage/health|https://football.standing.com/football-service/_manage/health|
| Environment |http://localhost:8080/football-service/_manage/env|https://dev.football.standing.com/football-service/_manage/env| https://test.football.standing.com/football-service/_manage/env| https://stage.football.standing.com/football-service/_manage/env|https://football.standing.com/football-service/_manage/env|  
| Information (Git) |http://localhost:8080/football-service/_manage/info|https://dev.football.standing.com/football-service/_manage/info|https://test.football.standing.com/football-service/_manage/info|https://stage.football.standing.com/football-service/_manage/info|https://football.standing.com/football-service/_manage/info|
| Swagger |http://localhost:8080/football-service/swagger-ui.html| https://dev.football.standing.com/football-service/swagger-ui.html| https://test.football.standing.com/football-service/swagger-ui.html|https://stage.football.standing.com/football-service/swagger-ui.html | https://football.standing.com/football-service/swagger-ui.html|
