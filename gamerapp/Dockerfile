FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN ./mvnw dependency:go-offline

COPY src ./src

RUN ./mvnw package -DskipTests

RUN chown -R appuser:appgroup /app

USER appuser

EXPOSE 8443


CMD ["sh", "-c", "keytool -genkeypair -alias gamerapp -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore /app/keystore.p12 -storepass \"$KEYSTORE_PASSWORD\" -dname 'CN=localhost, OU=GamerApp, O=GamerCommunity, L=Santiago, C=CL' -validity 365 -noprompt && java -jar target/gamerapp-0.0.1-SNAPSHOT.jar"]