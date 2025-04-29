FROM maven AS BUILDER

WORKDIR build

COPY src src

COPY pom.xml pom.xml

RUN mvn clean package -DskipTests

FROM bellsoft/liberica-openjdk-alpine:17.0.3

WORKDIR /usr/share/app

COPY --from=BUILDER /build/target/*.jar app.jar

CMD ["java", "-jar", "app.jar"]