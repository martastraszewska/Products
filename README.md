# Descrpition:

Application created to store products in CassandraDB.
The application is secured against unauthorized access, has got basic error handling in the backend,
is containerized using Docker and its operation are verified by tests.


# How to run application:

### 1. run cassandra database 
> `docker-compose up`

### 2. run application using IntelliJ or build docker image and add to docker-compose

# Build docker image:
> `mvn package`
>
> `docker build -t products .`







