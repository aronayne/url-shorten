
## Table of Contents

* [About the Project](#about-the-project)
  * [Built With](#built-with)
* [Getting Started](#getting-started)
  * [Prerequisites](#prerequisites)
  * [Installation](#installation)
* [Usage](#usage)



<!-- ABOUT THE PROJECT -->
## About The Project

This project converts a long URL to a short URL. The short URL can be used to open its associated long URL.


A long URL of max length 2000 characters is converted to a short URL of 7 characters. The probability of a short URL collision is ((1 + number of URL's added) / 62^7).


### Built With

This project is built with the following:

* [Docker](https://www.docker.com/)
* [Spring Boot](https://spring.io/projects/spring-boot)
* [PostgreSQL](https://www.postgresql.org/)
* [Apache Maven](https://maven.apache.org/)

## Getting Started

### Running and connecting as a single Docker instance

Steps to run PostgreSQL and Spring Boot in a single Docker instance.

1. Update src/main/resources application.properties, set DB to local: spring.datasource.url=jdbc:postgresql://postgres-db:5432/shorten-db

Run commands:
```sh
mvn package
3. docker-compose -f docker-compose-db-springboot.yml up --build
```

### Running Docker DB instance only (for local development)

1. Update src/main/resources application.properties, set DB to local: spring.datasource.url=jdbc:postgresql://localhost:5432/shorten-db

3. docker-compose -f docker-compose-db-only.yml up --build

### Connecting to Docker Database

1. In a separate command window open a prompt with: docker-compose exec postgres-db bash

2. enter on container : psql -U my_user -d shorten-db

3. Sample query: 
```sh
select * from url_store;
```

### Installation

#### IDE

1. Clone the repo
```sh
git clone https://github.com/aronayne/url-shorten
```
2. Open the project your IDE.

4. Download maven dependencies

4. Run the main Spring book class


#### Docker Install

1. Clone the repo
```sh
git clone https://github.com/aronayne/url-shorten
```

<!-- 2. Run commands:
```sh
mvn package
docker build -t url-shorten/url-shorten-docker .
docker run -p 8080:8080 url-shorten/url-shorten-docker
```
 -->
## Usage

### Generating and opening a shortened URL

Invoke Put request:

http://localhost:8080/shorten/?longUrl=https://www.google.com/ , the returned shortened value is used to generated a long URL.

To visit the page with the shortened URL enter the following into browser replacing SHORTENED_URL with the shortened value returned by previous Put request:

http://localhost:8080/shorten/redirect/<SHORTENED_URL>

### Swagger

After project install to view available services naviage to Swagger: 
http://localhost:8080/swagger-ui/ . 

To view swagger endpoints as JSON: http://localhost:8080/v2/api-docs




