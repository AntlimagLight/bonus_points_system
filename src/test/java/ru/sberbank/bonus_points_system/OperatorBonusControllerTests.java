package ru.sberbank.bonus_points_system;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.sberbank.bonus_points_system.test_util.JsonUtil;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.sberbank.bonus_points_system.test_util.TestData.*;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class OperatorBonusControllerTests extends BonusPointsSystemApplicationTests {

    @BeforeEach
    @Override
    void setup() {
        super.setup();
        login(OPERATOR_JWT_REQUEST);
    }


    @Test
    public void getAccountById() throws Exception {
        val userForSearchAcc = userRepository.findByLogin(OPERATOR.getLogin()).get();
        val searchAcc = bonusAccountRepository.findByUser(userForSearchAcc).get();
        this.mockMvc.perform(get("/operator/bonus_accounts/" + searchAcc.getId())
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(searchAcc.getId()))
                .andExpect(jsonPath("$.bonus").value(OPERATOR_BONUS_ACC.getBonus()))
                .andExpect(jsonPath("$.version").value(OPERATOR_BONUS_ACC.getVersion()));
    }

    @Test
    public void getAccountByIdNotFoundCase() throws Exception {
        this.mockMvc.perform(get("/operator/bonus_accounts/" + 0)
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.exception").value("ru.sberbank.bonus_points_system.exception.NotExistException"));
    }

    @Test
    public void getAccountByLogin() throws Exception {
        val userForSearchAcc = userRepository.findByLogin(OPERATOR.getLogin()).get();
        val searchAcc = bonusAccountRepository.findByUser(userForSearchAcc).get();
        this.mockMvc.perform(get("/operator/bonus_accounts/by-name?userName=" + OPERATOR.getLogin())
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(searchAcc.getId()))
                .andExpect(jsonPath("$.bonus").value(OPERATOR_BONUS_ACC.getBonus()))
                .andExpect(jsonPath("$.version").value(OPERATOR_BONUS_ACC.getVersion()));

    }

    @Test
    public void getAccountByLoginNotFoundCase() throws Exception {
        this.mockMvc.perform(get("/operator/bonus_accounts/by-name?userName=" + USER.getLogin())
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.exception").value("ru.sberbank.bonus_points_system.exception.NotExistException"));
    }

    @Test
    public void successfulOperatorDebit() throws Exception {
        val userForOperation = userRepository.findByLogin(OPERATOR.getLogin()).get();
        this.mockMvc.perform(patch("/operator/bonus_accounts/" + userForOperation.getId())
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(MINUS_OPERATION)))
                .andDo(print())
                .andExpect(status().isOk());
        val processingAcc = bonusAccountRepository.findByUser(userForOperation).get();
        assertEquals(processingAcc.getBonus(), OPERATOR_BONUS_ACC.getBonus().add(MINUS_OPERATION.getChange()));
        assertEquals(processingAcc.getVersion(), OPERATOR_BONUS_ACC.getVersion() + 1);
        val operation = bonusOperationMapper.toDto(bonusOperationRepository
                .findByExternalID(MINUS_OPERATION.getExternalID()));
        assertThat(operation)
                .usingRecursiveComparison()
                .ignoringFields("id", "dateTime")
                .isEqualTo(MINUS_OPERATION);
    }

    @Test
    public void tryAccrueOperation() throws Exception {
        val userForOperation = userRepository.findByLogin(OPERATOR.getLogin()).get();
        this.mockMvc.perform(patch("/operator/bonus_accounts/" + userForOperation.getId())
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(PLUS_OPERATION)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.exception").value("ru.sberbank.bonus_points_system.exception.IllegalAccrualOperation"));
        val processingAcc = bonusAccountRepository.findByUser(userForOperation).get();
        assertEquals(processingAcc.getBonus(), OPERATOR_BONUS_ACC.getBonus());
        assertEquals(processingAcc.getVersion(), OPERATOR_BONUS_ACC.getVersion());
    }

    // TODO Напоминание: реализовать отдельно тесты контроллера пользователя.

}
