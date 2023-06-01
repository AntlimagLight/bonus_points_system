package ru.sberbank.bonus_points_system.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;


@OpenAPIDefinition(
        info = @Info(
                title = "Bonus points system",
                description = """
                        Demo Rest API, made according to the test task of Sberbank.<br>
                        It is a crud service for working with bonus points.
                        
                        <b>Credentials:</b><br>
                        User: user_zero@yandex.ru / pass12<br>
                        User: user@yandex.ru / password<br>
                        Admin: admin@gmail.com / admin""",
                version = "1.0",
                contact = @Contact(
                        name = "Anton Dvorko",
                        email = "antlighter@yandex.ru"
                )
        )
)
//@SecurityScheme(
//        type = SecuritySchemeType.HTTP,
//        name = "basicAuth",
//        scheme = "basic")
public class SwaggerConfig {
}
