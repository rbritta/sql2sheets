# Sql 2 Sheets

Spring + Quartz application that connects to
a relational database, executes a sql query and
pushes the result to a Google Spreadsheet.

The database driver must be provided.

> Java 14; Spring Boot; Web Security; JPA Hibernate; Thymeleaf;
Bootstrap; JQuery; Quartz; LDAP; Google API / Spreadsheets;
Lombok; H2

## Application Configuration

+ Java 14 and Maven 3.6
+ Configure the `lib/` folder as project library in the IDE. The drivers can be put 
in this folder or uploaded on Application's frontend.

Authentication modes (application.yml):
* NONE: No authentication at all
* IN_MEMORY: Simple single user authentication (insecure)
* LDAP: Authentication with an external LDAP Server

## Google Sheets Configuration

To allow access to the spreadsheet, it's required a project 
and credential setup in the Google Developers console:
`http://console.developers.google.com`

Create a credential of type "Service Account" and get a Json Token. \
Share the spreadsheet with the Service Account email.

## Docker

See `Dockerfile` and `docker-compose.yml` files in this project. \
Also available on [hub.docker.com](https://hub.docker.com/r/rbritta/sql2sheets)




