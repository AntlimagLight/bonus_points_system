_Restaurant voting application_
===============================
Demo Rest API, made according to the test task of Sberbank. It is a crud service for working with bonus points.
Administrators can accrue and write off points from any user.
Operators can spend and deduct points from any user.
The user can only spend points from himself.

##### Current Version 1.0

[Project on GitHub](https://github.com/AntlimagLight/bonus_points_system)
Designed by [AntimagLight](https://www.linkedin.com/in/anton-dvorko-53a545263/)

------------------

- Stack: [JDK 17](http://jdk.java.net/17/), [Maven](https://maven.apache.org/), Spring Boot 3.0 (web, datajpa,
  validation, security, cache, tests), JUnit Jupiter API, Lombok, PostgreSQL, Mapstruct, Swagger, Docker.
- Run: ___________

------------------

## _Technical requirement_

Design a service for writing off and accruing bonus points for program customers.

- The service will be used by various clients.
- The balance of points cannot be less than zero.
- Protection against double write-offs should be provided.
- API should contain documentation and instructions for deployment.
- User information comes from another service.
- Code should be covered by tests.

------------------

## _REST API documentation_

[Link to Swagger](http://localhost:8080/swagger-ui/index.html)