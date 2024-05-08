FROM openjdk:17-jdk-alpine

RUN apk add --no-cache curl && \
    mkdir -p /usr/share/maven && \
    curl -fsSL http://apache.osuosl.org/maven/maven-3/3.8.8/binaries/apache-maven-3.8.8-bin.tar.gz | tar -xzC /usr/share/maven --strip-components=1 && \
    ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests
CMD ["java", "-jar", "target/exchangerates-converter-0.0.1-SNAPSHOT.jar"]
