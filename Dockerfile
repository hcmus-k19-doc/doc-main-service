FROM eclipse-temurin:17.0.5_8-jre-alpine
WORKDIR /app
COPY --from=MAVEN_BUILD /build/web/target/doc-main-service.jar /app/
EXPOSE 8080
ENTRYPOINT ["java","-jar","doc-main-service.jar"]
