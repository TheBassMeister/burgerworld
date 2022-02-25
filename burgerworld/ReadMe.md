# Welcome to the burgerworld application

This project is my reimplementation of my previous burger-cloud application. This time I will do it proper in an
iterative approach. I also plan to use this project to try out new things, like a frontend in React, using Protocol
Buffers for communication, and dockerize the separate parts of the application. The frontend and the backend would
optimally run in different services.

## Technologies used so far

- Spring Boot (Data and Web so far)
- Kotlin
- Maven
- MySQL as DB
- H2 for testing

## How to setup and run the application

### The DB connection

The code is written to run against a MySQL database. To be able to run it you should either install a MySQL database on
your machine, or even easier just spin up a MySQL Db in Docker. You can find the docker-compose.yml file in the
resources' folder to set up and start a MySQL DB. If you use it, please adjust it to your needs, but don't forget to
also update the application.yml appropriately

You should be able to use another DB as well, but the data.sql script might not work in other DB dialects. In the
default profile this project will only create the schemas but will not populate the database. If you want the database
to be populated, start the application in the dev profile.
<b>CAREFUL</b> the database will be repopulated by the data.sql on each start in the dev profile. Any new data you add
to the DB during testing, will be gone on a restart.

The tests are running using the in-memory H2 database. A MySql database connection will not be needed.
