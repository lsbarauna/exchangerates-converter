FROM eclipse-temurin:17-jdk-alpine
MAINTAINER BARAUNA
ENV HOSTNAME "0.0.0.0"
COPY ./exchangerates-converter/target/exchangerates-converter-0.0.1-SNAPSHOT.jar exchangerates-converter-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-Xverify:none", "-XX:TieredStopAtLevel=1", "-Xms768M", "-Xmx1024M", "-jar", "/exchangerates-converter-0.0.1-SNAPSHOT.jar"]
