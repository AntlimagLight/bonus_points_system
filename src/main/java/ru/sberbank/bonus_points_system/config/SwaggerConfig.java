package ru.sberbank.bonus_points_system.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;


@OpenAPIDefinition(
        info = @Info(
                title = "Bonus points system",
                description = """
                        Demo Rest API, made according to the test task of Sberbank.<br>
                        It is a crud service for working with bonus points.
                        To get started, you should log in, to do this:
                        1) call the method /auth/login
                        2) pass the username and password to it.
                        3) The access token received in response from the server can be used within 10 minutes.
                                                
                        <b>Credentials:</b><br>
                        User: user / pass12<br>
                        Operator: operator / pass34<br>
                        Admin: admin / admin56""",
                version = "1.0",
                contact = @Contact(
                        name = "Anton Dvorko",
                        email = "antlighter@yandex.ru"
                )
        )
)
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerConfig {
}
