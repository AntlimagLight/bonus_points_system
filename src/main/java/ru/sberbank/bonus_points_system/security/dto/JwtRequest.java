package ru.sberbank.bonus_points_system.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtRequest {

    @Schema(example = "admin")
    private String login;
    @Schema(example = "admin56")
    private String password;

}
