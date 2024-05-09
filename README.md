Exchangerates Converter
========


[API DOCS](https://exchangerates-converter-06663637310e.herokuapp.com/swagger-ui/index.html)

Rest API that is capable of converting between two currencies
using updated refresh rates from an external service (http://api.apilader.com/exchangerates_data/latest).
To perform the conversion, an API Key is required from the user who wishes to perform the conversion.
The API records each conversion transaction with all related information and also
provide an endpoint to query transactions performed by a user. The API supports the following symbols: BRL,USD,EUR,JPY 


Features - Available Endpoints
========

- /user Creates a new user associated with the API Key.
- /convert With this endpoint, we have any amount conversion from one currency to another.
- /transactions Returns all transactions executed by the user.
- /new-apikey Endipoint that allows the user to generate a new API Key.


Layers
========

Requirements
========

For building and running the application you need:

- [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven 3](https://maven.apache.org)

Building
========
Regular Maven build:

    mvn package

Build Docker container:

    docker build -t exchangerates-converter .


Running
=======
Java
----
Without Maven:

    java -jar target/exchangerates-converter-0.0.1-SNAPSHOT.jar

With maven:

     mvn spring-boot:run

Docker
------

    docker run -d -p 8080:8080 exchangerates-converter

IDE
------

    rum main class br.com.jaya.exchangerates.converter.ExchangeRatesApplication


About the Service
------

The REST service that performs the conversion between two currencies. It uses an in-memory database (H2) to store the data. 


Here is what this little application demonstrates:

* Full integration with the latest **Spring** Framework: inversion of control, dependency injection, etc.
* Packaging as a single war with embedded container (tomcat 8): No need to install a container separately on the host just run using the ``java -jar`` command
* Writing a RESTful service using annotation: support JSON request / response; 
* Exception mapping from application exceptions to the right HTTP response with exception details in the body
* *Spring Data* Integration with JPA/Hibernate with just a few lines of configuration and familiar annotations.
* Automatic CRUD functionality against the data source using Spring *Repository* pattern
* Demonstrates Junit test framework with associated libraries
* integration test with REST Assured
* All APIs are "self-documented" by Swagger3 using annotations
* Writing web services clients using FeignClient
* The code repository is the GitHub
* The cloud-as-a-service platform we adopted is Heroku
* Each push to main will deploy a new version of this application to Heroku. Deployments happen automatically.


Project architecture
========

The design pattern used in developing this API is called the "three-tier architecture pattern" or "MVC" (Model-View-Controller) pattern, 
often adapted in the Spring world as the "three-tier MVC" pattern.

- controller - Controllers are managing the REST interface, interacting with services to perform operations on data, and returning an appropriate response to the client.
- service - Contains business logic implementations, the middleware between Controller and Repository.
- repository - Repositories are interfaces that define methods for performing persistence operations on the database, such as saving, updating, deleting, and retrieving entities
- client - classe for making calls to RESTful services
- entity - Entities represent the domain objects in your application. In the context of Spring and JPA (Java Persistence API), entities are typically mapped to tables in the database.
- to - Is an object used to transfer data between application layers
- exception - Handles application Exception
- security - Handles application security and authentication
- configuration - Application Settings
- mapper - mappings between data transfer objects and domain entities
- util - Utilities Classes 

## Technology Choices

### We chose Spring Boot for the following reasons:

- Makes initial configuration easy: Spring Boot provides sensible default configurations for many common libraries and frameworks, allowing you to quickly start a project without requiring extensive manual configuration.
- Improved productivity: Spring Boot simplifies development by offering features such as dependency injection, convention-based development, and autoconfiguration. This reduces the amount of code you need to write and allows you to focus on business logic.
- Integration with Spring Ecosystem: By using Spring Boot, you have access to the entire Spring Framework ecosystem, including Spring Data, Spring Security, Spring MVC, among others. This provides a wide range of tools to build robust and secure applications.
- Ease of dependency management: Spring Boot uses Maven or Gradle for dependency management, which simplifies the management of libraries used in your application.
- Microservices and service-based architecture: Spring Boot is a popular choice for building microservices and service-based applications due to its ability to create lightweight, self-contained applications that can be easily packaged and deployed.

### We chose Lib MapStruct

Because it is a code generator that greatly simplifies implementing mappings between Java bean types based on a convention-over-configuration approach

### We chose Spring FeignClient

Simplified abstraction of RESTful calls, FeignClient provides a high-level abstraction for making calls to RESTful services. Eliminates the need to write HTTP client code manually, simplifying the consumption of web services

### We chose REST Assured as our integrated testing solution

REST Assured uses a simple and readable syntax, which resembles natural language, making it easier to write and understand evidence. This makes it easier for development and testing teams to collaborate on creating and maintaining API tests







