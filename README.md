# novel-ideas

![GitHub release (latest SemVer)](https://img.shields.io/github/v/release/klemmy129/novel-ideas?display_name=tag&sort=semver)
![GitHub top language](https://img.shields.io/github/languages/top/klemmy129/novel-ideas)
![GitHub](https://img.shields.io/github/license/klemmy129/novel-ideas)
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/klemmy129/novel-ideas)
![GitHub Workflow Status](https://img.shields.io/github/workflow/status/klemmy129/novel-ideas/CodeQL)
![Snyk Vulnerabilities for GitHub Repo (Specific Manifest)](https://img.shields.io/snyk/vulnerabilities/github/klemmy129/novel-ideas/pom.xml)
![GitHub issues](https://img.shields.io/github/issues/klemmy129/novel-ideas)

## Description  
This application is Rest backend is for managing data and information about a novel or script you want to write.

This is also a Java Spring Boot demonstrator for a Rest application.

The frontend Demo to this application is [novel-ideas-iu](https://github.com/klemmy129/novel-ideas-ui) an Angular 13 application.

### Technology Used
- Java 17
- Maven
- Spring Boot 2.6.6
- JPA/Hibernate
- FlyWay
  - Oracle 18c XE
  - Postgres 13
  - H2
- JUnit 5
- AssertJ
- Spring Docs
- Lombok
- [Certificates](CERTS.md)

## Building
Note: The Oracle and Postgres builds both can run with the H2 profile
### For Oracle
```
mvn clean package
```
### For PostgreSQL
```
mvn clean package -P postgres
```
### For H2
```
mvn clean package -P h2
```
### For Docker
_COMING SOON_

## Startup
### Active Profiles
* Oracle
`default,oracle`
* PostgreSQL
 `default,postgres`
* H2
`default,h2`

### Environment Variables
#### application-default.yml
- PORT _default 10443_
- TRUSTSTORE _default /home/${user}/certs/truststore.p12_
- TRUSTSTORE-PASSWORD
- KEYSTORE _default /home/${user}/certs/keystore.p12_
- KEYSTORE-PASSWORD

#### application-oracle.yml
- DB_URL _default jdbc:oracle:thin:@//localhost:1521/XEPDB1_
- DB_USERNAME _default novel_
- DB_PASSWORD _default novel1_

#### application-postgres.yml
- DB_URL _default jdbc:postgresql://localhost:5432/postgres_
- DB_USERNAME _default novel_
- DB_PASSWORD _default novel1_


## Why Maven Modules
### NOVEL-IDEAS-API
The reason I broke up the application so that it has an API module, is so I can share the DTO's
of this application with other developers. 
So they have access to my classes.

In the API's `pom.xml` the build plugins I configured to build a typescript module of the DTO classes for a NPM package.
This will help with front-end development.

## Coding Sample Explained - WIP
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

  public BookDto getBook(Integer id) {
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

I have also given examples on how I configured them in swagger eg 
[BookController](novel-ideas-rest/src/main/java/com/klemmy/novelideas/controller/BookController.java).

### Swagger
I have used SpringDocs in this demo, in other project in the passed I have used SpringFox, but SpringFox does not seem to being actively updated and maintained.

SpringDocs supports OpenApi standard 3. 

In my opinion a well documented swagger page is essential and refreshing to work with and in the long run will pay off for the effort.

Some examples I'll point too:
- [BookController](novel-ideas-rest/src/main/java/com/klemmy/novelideas/controller/BookController.java) - Controller
- [NovelIdeasApplication](novel-ideas-rest/src/main/java/com/klemmy/novelideas/NovelIdeasApplication.java) - the application startup
- [BookDto](novel-ideas-api/src/main/java/com/klemmy/novelideas/api/BookDto.java) - Dto Model

### Validation
_COMING SOON_
- Dto
- JPA
- Controller

### Authorisation
_COMING SOON_

### Auditing
_COMING SOON_

### Unit Testing
_Talking about Unit testing COMING SOON_

## Other Stuff
_COMING SOON_ I still need to think about it.

