_bonus_points_system_
===============================
Demo Rest API, made according to the test task of Sberbank. It is a crud service for working with bonus points.
Only authorized users can use the system.
To use the bonus account, the user must first register an account.

Administrators can accrue and debit points from any user.
Operators can debit points from any user.
The user can only spend points from himself.

##### Current Version 0.1

Note. This application implements only the necessary minimum + just a few tricks for convenience. 
For full-fledged comfortable operation of the system, it is necessary to expand the functionality with new methods.

For the correct operation of the application, an external PostgreSQL is required.
datasource.url=jdbc:postgresql://localhost:5432/bonus_points_db
username=admin
password=admin

Примечание. В планах было создать docker compose файл, добавить еще несколько методов и тестов.
В этой версии отправляю на обзор то, что успел сделать вечерами.

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
- Code should be covered by tests.

Bonus: Add an authorization implementation to the server using the JWT Token. 
User storage. Issuance of tokens. Validation of tokens.

------------------

## _REST API documentation_

[Link to Swagger](http://localhost:8080/swagger-ui/index.html)