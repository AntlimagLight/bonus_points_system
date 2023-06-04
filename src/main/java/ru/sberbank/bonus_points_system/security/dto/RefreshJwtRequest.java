package ru.sberbank.bonus_points_system.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshJwtRequest {

    @Schema(example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpdmFuIiwiZXhwIjoxNjg4NDYzNDQ5fQ.9AT75N3U3SBm-" +
            "GIExtchmAqOg8AH50i6CYvWfWCWONptKCPTqIlPsHbfo0UQA17K5fDYGFhRhaa6F8utNHsS1A")
    public String refreshToken;

}