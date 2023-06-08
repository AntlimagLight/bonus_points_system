package ru.sberbank.bonus_points_system.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequest {

    @Schema(example = "admin")
    @NotBlank
    @NotNull
    @Size(min = 3, max = 256)
    private String login;
    @Schema(example = "admin56")
    @NotBlank
    @NotNull
    @Size(min = 4, max = 256)
    private String password;

}
