package ru.sberbank.bonus_points_system;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.sberbank.bonus_points_system.dto.UserDto;
import ru.sberbank.bonus_points_system.test_util.JsonUtil;
import ru.sberbank.bonus_points_system.test_util.TestData;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.sberbank.bonus_points_system.test_util.TestData.*;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class AdminUserControllerTests extends BonusPointsSystemApplicationTests {

    @BeforeEach
    @Override
    void setup() {
        super.setup();
        login(ADMIN_JWT_REQUEST);
    }


    @Test
    void createUser() throws Exception {
        this.mockMvc.perform(post("/admin/users")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestData.jsonWithPassword(NEW_OPERATOR, NEW_OPERATOR.getPassword())))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        val fromDB = userMapper.toDto(userRepository.findByLogin(NEW_OPERATOR.getLogin()).get());
        assertThat(fromDB)
                .usingRecursiveComparison()
                .ignoringFields("id", "password", "registered", "enabled")
                .isEqualTo(NEW_OPERATOR);
    }

    @Test
    void createUserAlreadyExist() throws Exception {
        this.mockMvc.perform(post("/admin/users")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestData.jsonWithPassword(ALREADY_EXIST_USER, ALREADY_EXIST_USER.getPassword())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.exception").value("ru.sberbank.bonus_points_system.exception.AlreadyExistException"));
    }


    @Test
    void updateUser() throws Exception {
        val userForChange = userRepository.findByLogin(USER.getLogin()).get();
        this.mockMvc.perform(put("/admin/users/" + userForChange.getId())
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithPassword(NEW_OPERATOR, NEW_OPERATOR.getPassword())))
                .andDo(print())
                .andExpect(status().isOk());
        val fromDB = userMapper.toDto(userRepository.findByLogin(USER.getLogin()).get());
        val expected = new UserDto(null, NEW_OPERATOR.getName(), USER.getLogin(), "no matter", NEW_OPERATOR.getEmail(),
                NEW_OPERATOR.getRoles(), null, true);
        expected.setLogin(USER.getLogin());
        assertThat(fromDB)
                .usingRecursiveComparison()
                .ignoringFields("id", "password", "registered", "enabled")
                .isEqualTo(expected);
    }

    @Test
    void updateUserNotFoundCase() throws Exception {
        this.mockMvc.perform(put("/admin/users/" + 0)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithPassword(NEW_OPERATOR, NEW_OPERATOR.getPassword())))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.exception").value("ru.sberbank.bonus_points_system.exception.NotExistException"));
    }

    @Test
    void getUserById() throws Exception {
        val userForSearch = userRepository.findByLogin(USER.getLogin()).get();
        val resultActions = this.mockMvc.perform(get("/admin/users/" + userForSearch.getId())
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userForSearch.getId()))
                .andExpect(jsonPath("$.name").value(USER.getName()))
                .andExpect(jsonPath("$.login").value(USER.getLogin()))
                .andExpect(jsonPath("$.email").value(USER.getEmail()))
                .andExpect(jsonPath("$.enabled").value(userForSearch.getEnabled()));

        val json = resultActions.andReturn().getResponse().getContentAsString();
        val user = JsonUtil.readValue(json, UserDto.class);
        assertEquals(user.getRoles(), USER.getRoles());
    }

    @Test
    void getUserByIdNotFoundCase() throws Exception {
        this.mockMvc.perform(get("/admin/users/" + 0)
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.exception").value("ru.sberbank.bonus_points_system.exception.NotExistException"));

    }

    @Test
    void getUserByLogin() throws Exception {
        val userForSearch = userRepository.findByLogin(USER.getLogin()).get();
        val resultActions = this.mockMvc.perform(get("/admin/users/by-login?login=" + USER.getLogin())
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userForSearch.getId()))
                .andExpect(jsonPath("$.name").value(USER.getName()))
                .andExpect(jsonPath("$.login").value(USER.getLogin()))
                .andExpect(jsonPath("$.email").value(USER.getEmail()))
                .andExpect(jsonPath("$.enabled").value(userForSearch.getEnabled()));
        val json = resultActions.andReturn().getResponse().getContentAsString();
        val user = JsonUtil.readValue(json, UserDto.class);
        assertEquals(user.getRoles(), USER.getRoles());

    }

    @Test
    void getUserByLoginNotFoundCase() throws Exception {
        this.mockMvc.perform(get("/admin/users/by-login?login=" + NEW_USER.getLogin())
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.exception").value("ru.sberbank.bonus_points_system.exception.NotExistException"));
    }

    @Test
    void deleteUser() throws Exception {
        val userForDelete = userRepository.findByLogin(USER.getLogin()).get();
        this.mockMvc.perform(delete("/admin/users/" + userForDelete.getId())
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isOk());
        assertFalse(userRepository.existsByLogin(USER.getLogin()));


    }

    @Test
    void deleteNotFoundCase() throws Exception {
        this.mockMvc.perform(delete("/admin/users/" + 0)
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.exception").value("ru.sberbank.bonus_points_system.exception.NotExistException"));
    }

    @Test
    void deleteNotEnoughAuthorityCase() throws Exception {
        login(OPERATOR_JWT_REQUEST);
        val userForDelete = userRepository.findByLogin(USER.getLogin()).get();
        this.mockMvc.perform(delete("/admin/users/" + userForDelete.getId())
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.exception").value("org.springframework.security.access.AccessDeniedException"));
        assertTrue(userRepository.existsByLogin(USER.getLogin()));

    }

    @Test
    void setEnable() throws Exception {
        val userForBan = userRepository.findByLogin(USER.getLogin()).get();
        this.mockMvc.perform(patch("/admin/users/" + userForBan.getId() + "?enabled=false")
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isOk());
        assertFalse(userRepository.findByLogin(USER.getLogin()).get().getEnabled());
    }

    @Test
    void setEnableNotFoundCase() throws Exception {
        this.mockMvc.perform(patch("/admin/users/" + 0 + "?enabled=false")
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.exception").value("ru.sberbank.bonus_points_system.exception.NotExistException"));
    }


}
