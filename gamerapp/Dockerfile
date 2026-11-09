FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN ./mvnw dependency:go-offline

COPY src ./src

RUN keytool -genkeypair -alias gamerapp -keyalg RSA -keysize 2048 \
    -storetype PKCS12 -keystore src/main/resources/keystore.p12 \
    -storepass password123 -dname "CN=localhost, OU=GamerApp, O=GamerCommunity, L=Santiago, C=CL" -validity 365

RUN ./mvnw package -DskipTests

EXPOSE 8443

CMD ["java", "-jar", "target/gamerapp-0.0.1-SNAPSHOT.jar"]