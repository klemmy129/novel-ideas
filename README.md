# novel-ideas
**DRAFT - In work**

**Description:** This application is Rest backend is for managing data and information about a novel or script you want to write

This is also a Java Spring Boot demonstrator for a Rest application

**Technology Used:**
- Java 11
- Maven
- Spring Boot 2.6.x
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
###Active Profiles
* Oracle
`default,oracle`
* PostgreSQL
 `default,postgres`
* H2
`default,h2`
###Environment Variables
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

## Validation
_COMING SOON_

## Other Stuff
_COMING SOON_
???