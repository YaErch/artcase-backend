FROM openjdk:17

WORKDIR /app

COPY . .

RUN chmod +x mvnw && ./mvnw clean package

CMD ["java", "-jar", "target/*.jar"]