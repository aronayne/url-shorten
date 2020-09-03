
## Table of Contents

* [About the Project](#about-the-project)
  * [Built With](#built-with)
* [Getting Started](#getting-started)
  * [Prerequisites](#prerequisites)
  * [Installation](#installation)
* [Usage](#usage)

## About The Project

This project converts a long URL to a short URL. The short URL can be used to open its associated long URL.


A long URL of max length 2000 characters is converted to a short URL of 7 characters. The probability of a short URL collision is ((1 + number of URL's added) / 62^7).

### Built With

This project is built with the following:

* [Docker](https://www.docker.com/)
* [Spring Boot](https://spring.io/projects/spring-boot)
* [PostgreSQL](https://www.postgresql.org/)
* [Apache Maven](https://maven.apache.org/)

This project is tested with :

* [JUnit](https://junit.org/junit4/)
* [rest-assured](http://rest-assured.io/)
  
## Getting Started

### Running Spring Boot app & PostgreSQL in a single Docker container

Steps to run PostgreSQL and the url-shorten Spring Boot app in a single Docker instance:

```sh
git clone https://github.com/aronayne/url-shorten
```

Update src/main/resources application.properties, set DB to postgres-db: 
```sh
spring.datasource.url=jdbc:postgresql://postgres-db:5432/shorten-db
```

from the cloned repo dir location 'url-shorten' run commands:
```sh
mvn package
docker-compose -f docker-compose-db-springboot.yml up --build
```

### Running PostgreSQL DB in a Docker container to facilitate local development

```sh
git clone https://github.com/aronayne/url-shorten
```

Update src/main/resources application.properties, set DB to localhost: 
```sh
spring.datasource.url=jdbc:postgresql://localhost:5432/shorten-db
```

Run docker-compose with .yml file"
```sh
docker-compose -f docker-compose-db-only.yml up --build
```
#### IDE

Open the project in your IDE.

Download maven dependencies

Run the main Spring book class at location:
```sh
src/main/java/ShortenApplication.java
```

## Usage

Ensure Spring Boot app and DB is running as referenced in previous steps.

### Generating and opening a shortened URL

Invoke a Put request passing in the longUrl parameter which represents the URL to be shortened:

```sh
http://localhost:8080/shorten/?longUrl=https://www.google.com/
```

The returned shortened value is a mapping to the long URL and can be used to visit the long URL.

To visit the page associated using the short URL, enter the following URL into a browser (tested with Firefox) and replace SHORTENED_URL with the shortened value returned by the previous Put request:

```sh
http://localhost:8080/shorten/redirect/SHORTENED_URL
```

### Connecting to Docker PostgreSQL DB

Ensure postgres-db container is running. 

Open a prompt and connect to container using: 
```sh
docker-compose -f docker-compose-db-only.yml exec postgres-db bash
```

Enter on container :

```sh
psql -U my_user -d shorten-db
```

This project utilises one table, select query : 
```sh
select * from url_store;
```

## Swagger

![Screenshot](https://raw.githubusercontent.com/aronayne/public/6f826b65fc5c8aa317aa3e60934262298ead963e/Screenshot%202020-09-03%20at%2013.07.36.png)

After project install to view available services navigate to Swagger: 
```sh
http://localhost:8080/swagger-ui/ 
```

To view swagger endpoints as JSON: 
```sh
http://localhost:8080/v2/api-docs
```






