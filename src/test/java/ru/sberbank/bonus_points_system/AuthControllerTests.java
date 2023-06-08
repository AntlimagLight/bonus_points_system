package ru.sberbank.bonus_points_system;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.sberbank.bonus_points_system.security.dto.JwtResponse;
import ru.sberbank.bonus_points_system.security.dto.RefreshJwtRequest;
import ru.sberbank.bonus_points_system.test_util.JsonUtil;
import ru.sberbank.bonus_points_system.test_util.TestData;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.sberbank.bonus_points_system.test_util.TestData.*;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class AuthControllerTests extends BonusPointsSystemApplicationTests {


    @BeforeEach
    @Override
    void setup() {
        super.setup();
        login(ADMIN_JWT_REQUEST);
    }

    @Test
    void register() throws Exception {
        this.mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestData.jsonWithPassword(NEW_USER, NEW_USER.getPassword())))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        val fromDB = userMapper.toDto(userRepository.findByLogin(NEW_USER.getLogin()).get());
        assertThat(fromDB)
                .usingRecursiveComparison()
                .ignoringFields("id", "password", "registered", "enabled")
                .isEqualTo(NEW_USER);
    }

    @Test
    void registerAlreadyExist() throws Exception {
        this.mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestData.jsonWithPassword(ALREADY_EXIST_USER, ALREADY_EXIST_USER.getPassword())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.exception").value("ru.sberbank.bonus_points_system.exception.AlreadyExistException"));
    }

    @Test
    void loginNotFound() throws Exception {
        this.mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(NOT_FOUND_JWT_REQUEST)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.exception").value("ru.sberbank.bonus_points_system.exception.NotExistException"));
    }

    @Test
    void loginIncorrectPassword() throws Exception {
        this.mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(INCORRECT_PASS_JWT_REQUEST)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.exception").value("org.springframework.security.access.AccessDeniedException"));
    }


    @Test
    void getNewAccessToken() throws Exception {
        val resultActions = this.mockMvc.perform(post("/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(new RefreshJwtRequest(refreshToken))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        val json = resultActions.andReturn().getResponse().getContentAsString();
        val tokens = JsonUtil.readValue(json, JwtResponse.class);
        this.mockMvc.perform(get("/auth/info")
                        .header("Authorization", "Bearer " + tokens.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getNewAccessTokenByInvalidRT() throws Exception {
        this.mockMvc.perform(post("/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(new RefreshJwtRequest(INVALID_JWT_TOKEN))))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.exception").value("org.springframework.security.access.AccessDeniedException"));
    }

    @Test
    void getNewRefreshToken() throws Exception {
        val resultActions = this.mockMvc.perform(post("/auth/refresh")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(new RefreshJwtRequest(refreshToken))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        val json = resultActions.andReturn().getResponse().getContentAsString();
        val tokens = JsonUtil.readValue(json, JwtResponse.class);
        this.mockMvc.perform(get("/auth/info")
                        .header("Authorization", "Bearer " + tokens.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        this.mockMvc.perform(post("/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(new RefreshJwtRequest(tokens.getRefreshToken()))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getNewRefreshTokenByInvalidRT() throws Exception {
        this.mockMvc.perform(post("/auth/refresh")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(new RefreshJwtRequest(INVALID_JWT_TOKEN))))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.exception").value("org.springframework.security.access.AccessDeniedException"));
    }

    @Test
    void getAuthInfo() throws Exception {
        val testingUser = userRepository.findByLogin("admin").get();
        this.mockMvc.perform(get("/auth/info")
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.authenticated").value("true"))
                .andExpect(jsonPath("$.login").value(testingUser.getLogin()))
                .andExpect(jsonPath("$.userId").value(testingUser.getId()))
                .andExpect(jsonPath("$.name").value(testingUser.getName()));
    }
}
