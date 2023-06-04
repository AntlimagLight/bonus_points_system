package ru.sberbank.bonus_points_system.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.sberbank.bonus_points_system.dao.Role;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class UserDto {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotBlank
    @Size(min = 3, max = 128)
    @Schema(example = "Иван")
    private String name;
    @NotNull
    @Size(min = 3, max = 256)
    @Schema(example = "admin")
    private String login;
    @NotBlank
    @Size(min = 4, max = 256)
    @Schema(accessMode = Schema.AccessMode.WRITE_ONLY, example = "admin56")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @Email
    @NotBlank
    @Schema(example = "email@gmail.com")
    private String email;
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDate registered;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Boolean enabled;

}
