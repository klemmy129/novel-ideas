# novel-ideas

![GitHub release (latest SemVer)](https://img.shields.io/github/v/release/klemmy129/novel-ideas?display_name=tag&sort=semver)
![GitHub top language](https://img.shields.io/github/languages/top/klemmy129/novel-ideas)
![GitHub](https://img.shields.io/github/license/klemmy129/novel-ideas)
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/klemmy129/novel-ideas)
![GitHub issues](https://img.shields.io/github/issues/klemmy129/novel-ideas)
[![CodeQL](https://github.com/klemmy129/novel-ideas/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/klemmy129/novel-ideas/actions/workflows/codeql-analysis.yml)
![GitHub Workflow Status](http://img.shields.io/github/actions/workflow/status/klemmy129/novel-ideas/codeql-analysis.yml?branch=main)
[![Dependency Review](https://github.com/klemmy129/novel-ideas/actions/workflows/dependency-review.yml/badge.svg)](https://github.com/klemmy129/novel-ideas/actions/workflows/dependency-review.yml)

## Description  
This application is Rest backend for managing data and information about a novel or script you want to write.

This is also a Java Spring Boot demonstrator for a Rest application.

I have also created a second Java Spring Boot demonstrator [novel-ghostwriter](https://github.com/klemmy129/novel-ghostwriter), 
this is used to show how to a http client communicates with novel-idears and AMPQ events. 

The frontend Demo to this application is [novel-ideas-iu](https://github.com/klemmy129/novel-ideas-ui) an Angular 15 application.

## Table of Contents
<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->

- [Technology Used](#technology-used)
- [Building](#building)
  - [For Oracle](#for-oracle)
  - [For PostgreSQL](#for-postgresql)
  - [For H2](#for-h2)
  - [Message Bus](#message-bus)
    - [ActiveMQ Artemis](#activemq-artemis)
      - [Setup](#setup)
      - [Info](#info)
    - [RabbitMQ](#rabbitmq)
    - [Kafka](#kafka)
  - [For Docker](#for-docker)
- [Startup](#startup)
  - [Active Profiles](#active-profiles)
  - [Environment Variables](#environment-variables)
    - [application.yml](#applicationyml)
    - [application-oracle.yml](#application-oracleyml)
    - [application-postgres.yml](#application-postgresyml)
    - [application-activemq.yml](#application-activemqyml)
    - [application-stomp.yml](#application-stompyml)
  - [Running it](#running-it)
  - [Viewing it](#viewing-it)
    - [Swagger](#swagger)
    - [Novel-Ideas-UI (Draft)](#novel-ideas-ui-draft)
    - [Actuator](#actuator)
- [Coding Demo Explained](#coding-demo-explained)
  - [Why Maven Modules](#why-maven-modules)
    - [novel-ideas-api](#novel-ideas-api)
    - [novel-ideas-client](#novel-ideas-client)
    - [novel-ideas-client3](#novel-ideas-client3)
    - [novel-ideas-autoconfig](#novel-ideas-autoconfig)
    - [novel-ideas-client-starter](#novel-ideas-client-starter)
    - [novel-ideas-rest](#novel-ideas-rest)
  - [Using the artifacts in another application](#using-the-artifacts-in-another-application)
  - [Constructor-based Dependency Injection](#constructor-based-dependency-injection)
  - [Enum](#enum)
  - [JPA Specification](#jpa-specification)
  - [Paging](#paging)
  - [Swagger annotations](#swagger-annotations)
  - [Java Record vs Lombok](#java-record-vs-lombok)
  - [Message Bus](#message-bus-1)
  - [Banner](#banner)
  - [Error Handling](#error-handling)
  - [Validation](#validation)
  - [Authorisation](#authorisation)
  - [Auditing](#auditing)
  - [Unit Testing](#unit-testing)
- [Other Stuff](#other-stuff)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## Technology Used
Note: All sample paths in this project are using Linux base.
- Java 21
- Maven
- Spring Boot 3.3.1
- JPA/Hibernate
- JDBC Template
- JDBC Client
- FlyWay
  - Oracle 21c XE / Oracle 23c Free
  - Postgres 13
  - H2
- ActiveMQ 6 (Artemis) (Commented out)
- JUnit 5
- AssertJ
- Spring Docs
- Lombok
- [Certificates](CERTS.md)

## Building

**Note:** 
* The Oracle and Postgres builds both can run with the H2 profile
* You can use `package` instead of `install`, if you are not running [novel-ghostwriter](https://github.com/klemmy129/novel-ghostwriter).

### For Oracle
```shell
mvn clean install
```
### For PostgreSQL
```shell
mvn clean install -P postgres
```
### For H2
```shell
mvn clean install -P h2
```
### Message Bus (Commented out - not using)
#### ActiveMQ Artemis
##### Setup
1. Download ActiveMQ Artemis binary from https://activemq.apache.org/download.html
2. Uncompress the file. I recommend `/opt` and set `${ARTEMIS_HOME}` to the base path.
3. Then in a terminal you need to create a broker. It will ask for a password
```shell
cd /var/lib
${ARTEMIS_HOME}/bin/artemis create mybroker
```
##### Info
- To Start: `/var/lib/mybroker/bin/artemis run`
- To Stop" `/var/lib/mybroker/bin/artemis stop`
- Web Management Console: http://localhost:8161/console/auth/login
- Documentation I used was: https://activemq.apache.org/components/artemis/documentation/latest/

### For Docker
Added a basic docker file, but currently needs Kubernetes map the certificates into the Pod.

This is just the start as I'm creating a new repo for Kubernetes using [Kind](https://kind.sigs.k8s.io/) and the setup and mangement using [Helm Charts](https://helm.sh/). 

_MORE COMING SOON_

## Startup

### Active Profiles
* Oracle
`oracle`
* PostgreSQL
 `postgres`
* H2
`h2`
* ActiveMQ `activemq`
* STOMP `stomp`

Eg `oracle,activemq,stomp`

### Environment Variables
#### application.yml
- PORT _default 10443_
- TRUSTSTORE  _default /home/${user}/certs/myCA.pem_
- KEYSTORE-PUB _default /home/${user}/certs/devcert.pem_
- KEYSTORE-KEY _default /home/${user}/certs/devcert.key_
- KEYSTORE-PASSWORD

##### Note 
##### Spring Boot's SSL bundles
I have moved from `server.ssl` certificates configuration to "Spring Boot's SSL bundles".
In the past Java required binary key store e.g. JKS or PKCS#12. 
Now you can use binary or text base Certificates i.e. PEM file.
Spring Boot automates the creation of JKS like file for Java to use.
In the example below it list all the certificates and passwords the app needs, 
but down at the `server.ssl.bundle` I register the bundle that was configured in the `spring` section.

Also under `options` I have configured the security protocol (TSL1.3) and the ciphers I want it to only use.

I use a tool called `testssl` to validate my certificates and web server security configuration.

Example:
```yaml
spring:
  ssl:
    bundle:
      pem:
        web-server:
          truststore:
            certificate: ${TRUSTSTORE:/home/${user}/certs/myCA.pem}
          keystore:
            certificate: ${KEYSTORE-PUB:/home/${user}/certs/devcert.pem}
            private-key: ${KEYSTORE-KEY:/home/${user}/certs/devcert.key}
            private-key-password: ${KEYSTORE-PASSWORD}
          options:
            ciphers:
              - TLS_AES_256_GCM_SHA384
              - TLS_CHACHA20_POLY1305_SHA256
            enabled-protocols: TLSv1.3
server:
  servlet:
    context-path: "/"
  port: ${PORT:10443}
  http2:
    enabled: true
  ssl:
    bundle: "web-server"
```
Code using RestTemplate with bundle Example:
```java
 public RestConfiguration(RestTemplateBuilder restTemplateBuilder, SslBundles sslBundles) {
    this.restTemplate = restTemplateBuilder.setSslBundle(sslBundles.getBundle("web-server")).build();
  }
```
or RestClient using bundle Example [NovelIdeasService](novel-ideas-autoconfiguration/src/main/java/com/klemmy/novelideas/config/NovelIdeasService.java) and [NovelIdeasClient3](novel-ideas-client3/src/main/java/com/klemmy/novelideas/client3/NovelIdeasClient3.java):
```java
...
RestClient restClient = restClientBuilder
        .baseUrl(novelIdeasClientProperties.url())
        .apply(restSsl.fromBundle("web-server"))
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();
...
HttpServiceProxyFactory proxyFactory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build();
return proxyFactory.createClient(NovelIdeasClient3.class);
```

#### application-oracle.yml
- DB_URL _default jdbc:oracle:thin:@//localhost:1521/XEPDB1_
- DB_USERNAME _default novel_
- DB_PASSWORD _default novel1_

#### application-postgres.yml
- DB_URL _default jdbc:postgresql://localhost:5432/postgres_
- DB_USERNAME _default novel_
- DB_PASSWORD _default novel1_

#### application-activemq.yml (not using)
- MBUS_URL _default localhost

#### application-stomp.yml (not using)
- MBUS_URL _default 127.0.0.1
- BUSPOST _default 61613
- WS_USERNAME _default guest
- WS_PASSWD _default guest

### Running it
I normally run it up in my IDE. eg Intellij or VSCode

or command line eg: `java -Dspring.profiles.active=oracle,activemq  -jar ./novel-ideas-rest/target/novel-ideas-rest-1.4.0.jar`

and you will need to define your system's environment variables, from the above section.

### Viewing it
#### Swagger
In a Web browser: 
* Address based on the CERT URL you created:
https://servername.devstuff.org:10443/swagger-ui/index.html or
* Local address:
https://localhost:10443/swagger-ui/index.html

#### Novel-Ideas-UI (Draft)
The frontend Demo to this application is [novel-ideas-iu](https://github.com/klemmy129/novel-ideas-ui) an Angular application. (I have not done much)

#### Actuator
I'm not going to go into Spring Boot Actuators.

Here is a very simple example: https://localhost:10443/actuator/health/

That should report:
```json
{
"status": "UP"
}
```

You can configure many more, but be warned, think of security.

Spring Boot Actuators documentation: https://docs.spring.io/spring-boot/docs/3.0.6/reference/html/actuator.html#actuator.enabling


## Coding Demo Explained
### Why Maven Modules
#### novel-ideas-api
The reason I broke up the application so that it has an API module is, so I can share the DTO's
of this application with other developers. 
So they have access to my classes.

In the API's `pom.xml` the build plugins I configured to build a typescript module of the DTO classes for a NPM package.
This will help with front-end development.

#### novel-ideas-client
I added novel-ideas-client module, but I could have just added the client class into the novel-ideas-api module.
This client would be used in other Java applications. 
This Class would have a handful of methods that would call the rest endpoint URL, properly formatted. 
Making it easier for the developer. I put a small sample [NovelIdeasClient](novel-ideas-client/src/main/java/com/klemmy/novelideas/client/NovelIdeasClient.java). 

#### novel-ideas-client3
I added novel-ideas-client3 module, but I could have just added the client class into the novel-ideas-api module.
I have used the new (in Spring Boot 3.0+) declarative interface for Web Client, opposed to RestTemplate pattern.
This uses the new annotation `@GetExchange`. There are more, but I did not use them in this demo.
This Class would have a handful of methods that would call the rest endpoint URL, properly formatted.
Making it easier for the developer. I put a small sample [NovelIdeasClient3](novel-ideas-client3/src/main/java/com/klemmy/novelideas/client3/NovelIdeasClient3.java).


#### novel-ideas-autoconfig
This module all the beans needed to start both novel-ideas-client and novel-ideas-client3 in another Java application like [novel-ghostwriter](https://github.com/klemmy129/novel-ghostwriter) 
The Property and Configuration classes used with the client are using multiple annotations:

**Used by Property Class/Record**
* `@ConfigurationProperties(prefix = "server.ssl")` This is used populate the class or record with config data in your yaml file based on the prefix.
* `@ConstructorBinding` Is used in property classes to indicate that configuration properties should be bound using constructor arguments rather than by calling setters. As I'm using a record this is needed. Java `record` don't have setters.

**Used by Configuration Classes**
* `@Configuration` classes can contain bean definition methods that are annotated with `@Bean`
* `@EnableConfigurationProperties(SslProperties.class)` This binds the property POJO class/record.
* `@ConditionalOnProperty(name = "novel-ideas.url")` This will load the Bean if true. These other two element can also be useful `matchIfMissing` or `haveingValue`. There are many @Conditional*xxxx* annotations.
* `@Bean(name="ssl")` When you have multiple bean that will load and do similar functionality, you have to name them. 
* `@Qualifier("ssl")` I have 2x Beans could be used as `RestTemplate` bean, so I have named them, the `@Qualifier` specifies which one to use. 
* `@DependsOn("ssl")` Bean creation can you unpredictable, this is one way you can make sure your bean has what it needs to create itself. 

#### novel-ideas-client-starter
Just a `pom.xml` that you would include in another Java application that you are setting up the client via the Autoconfig to call this application.

#### novel-ideas-rest
This module has RestControllers, Services, JPA repositories,specifications, models used with the repositories, DAO, DTO Factories, configuration to help setup the application context and main to start the application.

The controllers are the inputs and outputs to the public. Service are the business logic. 
The Factories convert the API DTOs used be the controllers and transforms them into JPA models for the repositories. 

This module has the JPA repositories, specifications and models used with the repositories.

Some of the JPA repositories queries use specifications.

These models have jakarta persistence and validation annotations to help model the table structure in the database.

JPA (Jakarta Persistence API, formerly Java Persistence API) the framework I have used is Hibernate.
Hibernate is an Object-Relational Mapping (ORM). JPA lets you avoid the need to “think relationally", opposed to JDBC.

This also has the flyway script in the resources. These are usually sql scripts that create and manipulate tables and other database objects

### Using the artifacts in another application
For another Java application to use is, they would:
1. Add the novel-idea-api to you POM file (or novel-idea-autoconfig which loads the novel-idea-client and novel-idea-api if the autoconfig condition were met).
2. NovelIdeasClient requires 2x parameters:
   1. Base URL: Of the running Novel-ideas-rest application. I would load this via a property class referring to a yaml property file.
   2. RestTemplate: This class could be very simple or more complex like, load certificates to call it via https, adding header, OAuth2, etc.
3. Then write a spring `@Configuration` class to create an instance of the client.


### Constructor-based Dependency Injection
I use Constructor-based Dependency Injection over Spring's `@Autowire`

Pros
- It makes unit testing easier. You can use the `new` keyword, so you do not depend on reflection.
- You can use `final` which assists with thread safety and final feels more stable. 
- It makes it immutable.

Cons
- More code, but I use Lombok's `@AllArgsConstructor`.

Example:
```java
@AllArgsConstructor
public class BookController {
  private final BookService bookService;

  public BookDto getBook(Long id) {
    return bookService.loadBook(id);
  }
}
```

### Enum
[BookState](novel-ideas-api/src/main/java/com/klemmy/novelideas/api/BookState.java) is the only Enum that has been created in this project.
A display value was added to show a more english output. 

eg enum = "ON_HOLD", display value = "On Hold"

The `toString()` caused issues in the unit testing. When comparing results, it would fail stating `[ACTIVE]` did not match `[Active]`.
THe json return was the enum, but when I put an enum and converted it to a string it used the display value.  

### JPA Specification
I have given two different example of how you can use a Specification in the JPA query.

I created Specification methods in
[FilterBookSpecification](novel-ideas-jpa/src/main/java/com/klemmy/novelideas/jpa/specification/FilterBookSpecification.java) for
[BookRepository](novel-ideas-jpa/src/main/java/com/klemmy/novelideas/jpa/repository/BookRepository.java) using `where` and `and` to make the query. The three methods deal with:

- String search using `like`
- Searching for an Enum using `equal`
- Searching for a date with a date range, using `between`

I created Specification class 
[FilterCharacterSpecification](novel-ideas-jpa/src/main/java/com/klemmy/novelideas/jpa/specification/FilterCharacterSpecification.java) for
[CharacterProfileRepository](novel-ideas-jpa/src/main/java/com/klemmy/novelideas/jpa/repository/CharacterProfileRepository.java) this overrides the `toPredicate` method to create the query.
The main features in this example are:
- Joining of two other tables using `Join`. Only when needed.
- Used the `or` so the search string search similar fields that might have the data.
- Converted comparisons to lower case in both specifications

These are two very different examples and all parameters are optional.

I have used them a filter from a Controllers inputs. Note you can have no fields, some fields or all the fields used to search the database

### Paging
I have examples of how simple paging is from Controller to Service to Repository.

I have also given examples on how I configured them in swagger e.g. 
[BookController](novel-ideas-rest/src/main/java/com/klemmy/novelideas/controller/BookController.java).

In the [NovelIdeasClient](novel-ideas-client/src/main/java/com/klemmy/novelideas/client/NovelIdeasClient.java) 
I could not use the interface `Page` or the class `PageImpl` as it contains no default constructors. 
This is a problem when Jackson is trying to deserialize class. So I extended `PageImpl`and created [RestPage](novel-ideas-api/src/main/java/com/klemmy/novelideas/util/RestPage.java). It is not pretty but it works in the client. 

### Swagger annotations
I have used SpringDocs in this demo, in other project in the past I have used SpringFox, but SpringFox does not seem to being actively updated and maintained.

SpringDocs supports OpenApi standard 3. 

In my opinion a well documented swagger page is essential and refreshing to work with and in the long run will pay off for the effort.

Some examples I'll point too:
- [BookController](novel-ideas-rest/src/main/java/com/klemmy/novelideas/controller/BookController.java) - Controller
- [NovelIdeasApplication](novel-ideas-rest/src/main/java/com/klemmy/novelideas/NovelIdeasApplication.java) - the application startup
- [BookDto](novel-ideas-api/src/main/java/com/klemmy/novelideas/api/BookDto.java) - Dto Model

### Java Record vs Lombok
I'm not a fan of a lot of boiler-plating code.

This is where Lombok's annotations helps with this.

Making the code look cleaner and more readable.

I personally love how Lombok's helps with this. I'm a fan of:
- @Getters
- @Setters
- @Builder
- @AllArgsConstructor
- @NoArgsConstructor

But, the Java community is uncertain about Lomboks future. 

Java 14 released a new type called: `record`.

From what I can see in the community, Java's `record` looks suitable for small immutable classes. 
But, I believe Lombok is still the best option, at this time, for large complex DTOs.

So I have converted 2x small simple DTOs from Lombok's annotations to Java's`record`
- [CharacterGenderDto](novel-ideas-api/src/main/java/com/klemmy/novelideas/api/CharacterGenderDto.java)
- [CharacterImportanceDto](novel-ideas-api/src/main/java/com/klemmy/novelideas/api/CharacterImportanceDto.java)

This change had flow-on effect throughout the code, especially factorys and unit tests.

And here are some Property files
- [NovelIdeasClientProperties](novel-ideas-autoconfiguration/src/main/java/com/klemmy/novelideas/config/NovelIdeasClientProperties.java)
- [SslProperties](novel-ideas-autoconfiguration/src/main/java/com/klemmy/novelideas/config/SslProperties.java)

### Message Bus (Commented Out - Not using)
Novel-Ideas is the producer for a message bus and [novel-ghostwriter](https://github.com/klemmy129/novel-ghostwriter) is the consumer of the messages off the bus and displays it to the console. 

By default, I have set `message-bus:type: none` which will load a dummy stub bean.
I made it an auto-configurable to be able to be a "producer" and put messages on the message bus,
by adding `activemq` to the profile. This sets `message-bus:type: activemq`. It also has the broker URL and the topic name.

I used a property and configure classes to load and setup a Connection Factory and  JMS template.
I then setup an interface called `MessageBus` that is implemented on the dummy stub that does nothing and the other will put messages on the bus.
The `messageBus` interface is loaded in the `BookService` as a construction parameter.

It is triggered by calling the Rest endpoint: Get `/book/{id}`

### Banner

I create a custom banner file, named `banner.txt` in the `src/main/resources` with the yaml files.

Note banner.txt is the default expected banner file name, which Spring Boot uses. 
However, if we want to choose any other location or another name for the banner, 
we need to set the spring.banner.location property in the `application.yml` file:
```yaml
banner:
  location: classpath:/path/mybanner.txt
```

There are many ASCII Art Generator's out there. 

I used (first one I clicked): http://patorjk.com/software/taag/#p=display&f=Big%20Money-ne&t=Type%20Something%20

Others:
- https://devops.datenkollektiv.de/banner.txt/index.html
- http://www.network-science.de/ascii/
- https://textkool.com/en/ascii-art-generator?hl=default&vl=default&font=Mike&text=stacktraceguru

Spring Boot 3.0+ no longer supports image types.

### Error Handling
For my RestControllers Error handling I have [ErrorController](novel-ideas-rest/src/main/java/com/klemmy/novelideas/controller/error/ErrorController.java) that I have annotated with `@RestControllerAdvice`

In here I register the error classes I with to handle by annotating `@ExceptionHandler` and the error class I'm intercepting.

Spring Boot 3.0+ has introduced a Problem Details class for HTTP APIs, this implements [RFC 7807](https://www.rfc-editor.org/rfc/rfc7807) specification. In simple terms is defines a common error format that is flexible.

So I have changed my error data handling of to `ProblemDetails`. This allowed me to delete 2x API classes I created: `ValidationErrorResponse` and `Violation`.

What I like about this new class was I could create more fields/properties to help describe to problem better, with the method `setProperty(String name, Object value)`

You could also set the type. Which is URI to a page to explain the error in more detail.

I also created my own Error exception class [FindDataException](novel-ideas-api/src/main/java/com/klemmy/novelideas/error/FindDataException.java), this extends the `Exception` class. 

### Validation
_COMING SOON_
- Dto
- JDBC
- Controller

### Authorisation
_COMING SOON_

### Auditing
_COMING SOON_

### Unit Testing
_Talking about Unit testing COMING SOON_

## Other Stuff
_COMING SOON_ I still need to think about it.

