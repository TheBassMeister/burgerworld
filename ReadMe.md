# Welcome to the burgerworld application

This project is my reimplementation of my previous burger-cloud application. This time I will do it proper in an
iterative approach. I also plan to use this project to try out new things, like a frontend in React, using Protocol
Buffers for communication, and dockerize the separate parts of the application. The frontend and the backend would
optimally run in different services.

## Technologies used so far

- Spring Boot (Data, Rest and Web so far)
- Kotlin
- Maven
- MySQL as DB
- H2 as DB for testing
- Swagger (Open API 3)
- Docker

## How to setup and run the application

### The recommended Docker way
The easiest way to locally start the burgerworld application is to use Docker. For your convenience a Docker Compose file
and the DockerFile is provided in the root directory of the project. To run it use the following steps:

1. Install Docker (e.g. Docker for Desktop on Windows)
2. Build the application (e.g. mvn install) so that the burgerworld jar file will be in the target directory
3. Navigate to the root directory and simply type in ```docker compose up``` and the MySQL db and the app will be deployed.
Please not that there is some Hibernate error on the first run, I will need to fix it sometimes, but it is a non breaking error and is gone
once you restart the burgerworld app
4. After this there should be a burgerworld container in Docker Desktop (if you have installed it) where you can stop and start the services.
You will not need to use ```docker compose up``` again.
5. The REST api should be reachable under your hostname:8080 (e.g. localhost:8080), see endpoints below

<b>Careful:</b> Running the application this way will start it in the dev profile where all data will be 
cleared on a restart. For example any burger you added during testing will be gone after a restart.

### The classical way (without Docker)

If you do not want to use Docker, you will have to install a MySQL database with a burgerworld
database. Please adjust the application.yml in your way.

You should be able to use another DB as well, but the data.sql script might not work in other DB dialects. In the
default profile this project will only create the schemas but will not populate the database. If you want the database
to be populated, start the application in the dev profile.
<b>CAREFUL</b> the database will be repopulated by the data.sql on each start in the dev profile. Any new data you add
to the DB during testing, will be gone on a restart.

The tests are running using the in-memory H2 database. A MySql database connection will not be needed.

### Rest Endpoints
- Swagger => http://[Your Host:Port]/swagger-ui/index.html
- Burgers => http://[Your Host:Port]/burgers
- Ingredients => http://[Your Host:Port]/ingredients

### Security Restrictions
Only users with ROLE_USER can post new burgers. Only users with ROLE_ADMIN can delete burgers.
Please check in the code for example users.




