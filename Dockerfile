FROM gradle:7-alpine AS build
WORKDIR /app
COPY . .

USER root                
RUN chown -R gradle /app 
USER gradle              

RUN gradle wrapper
RUN ./gradlew build

FROM openjdk:17-jdk-alpine AS prod
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY --chown=spring:spring --from=build app/build publish
WORKDIR /publish
RUN ls -l
USER root
RUN chmod -R 777 /tmp
RUN ls -l
EXPOSE 8080
ENTRYPOINT ["java","-jar","./libs/api-0.0.1-SNAPSHOT.jar","pl.edu.wat.repo.api.ApiApplication"]