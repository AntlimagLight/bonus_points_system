package ru.sberbank.bonus_points_system;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.sberbank.bonus_points_system.dto.BonusAccountDto;
import ru.sberbank.bonus_points_system.test_util.JsonUtil;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.sberbank.bonus_points_system.test_util.TestData.*;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class AdminBonusControllerTests extends BonusPointsSystemApplicationTests {

    @BeforeEach
    @Override
    void setup() {
        super.setup();
        login(ADMIN_JWT_REQUEST);
    }

    @Test
    public void createAccount() throws Exception {
        val userForCreateAcc = userRepository.findByLogin(USER.getLogin()).get();
        this.mockMvc.perform(post("/admin/bonus_accounts?userId=" + userForCreateAcc.getId())
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isCreated());
        val fromDB = bonusAccountMapper.toDto(bonusAccountRepository.findByUser(userForCreateAcc).get());
        val expected = new BonusAccountDto(fromDB.getId(), new BigDecimal(0),
                0, null, userMapper.toDto((userForCreateAcc)));
        assertThat(fromDB)
                .usingRecursiveComparison()
                .ignoringFields("lastUpdate", "user.password", "user.roles")
                .isEqualTo(expected);
    }

    @Test
    public void createAccountAlreadyExistCase() throws Exception {
        val userForCreateAcc = userRepository.findByLogin(OPERATOR.getLogin()).get();
        this.mockMvc.perform(post("/admin/bonus_accounts?userId=" + userForCreateAcc.getId())
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.exception").value("ru.sberbank.bonus_points_system.exception.AlreadyExistException"));

    }


    @Test
    public void successfulOperation() throws Exception {
        val userForOperation = userRepository.findByLogin(OPERATOR.getLogin()).get();
        this.mockMvc.perform(patch("/admin/bonus_accounts/" + userForOperation.getId())
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(PLUS_OPERATION)))
                .andDo(print())
                .andExpect(status().isOk());
        val processingAcc = bonusAccountRepository.findByUser(userForOperation).get();
        assertEquals(processingAcc.getBonus(), OPERATOR_BONUS_ACC.getBonus().add(PLUS_OPERATION.getChange()));
        assertEquals(processingAcc.getVersion(), OPERATOR_BONUS_ACC.getVersion() + 1);
        val operation = bonusOperationMapper.toDto(bonusOperationRepository
                .findByExternalID(PLUS_OPERATION.getExternalID()));
        assertThat(operation)
                .usingRecursiveComparison()
                .ignoringFields("id", "dateTime")
                .isEqualTo(PLUS_OPERATION);
    }

    @Test
    public void notEnoughBalanceOperation() throws Exception {
        val userForOperation = userRepository.findByLogin(OPERATOR.getLogin()).get();
        this.mockMvc.perform(patch("/admin/bonus_accounts/" + userForOperation.getId())
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(BIG_MINUS_OPERATION)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.exception").value("ru.sberbank.bonus_points_system.exception.InsufficientBonusException"));
        val processingAcc = bonusAccountRepository.findByUser(userForOperation).get();
        assertEquals(processingAcc.getBonus(), OPERATOR_BONUS_ACC.getBonus());
        assertEquals(processingAcc.getVersion(), OPERATOR_BONUS_ACC.getVersion());
    }

    @Test
    public void doubleOperation() throws Exception {
        val userForOperation = userRepository.findByLogin(OPERATOR.getLogin()).get();
        this.mockMvc.perform(patch("/admin/bonus_accounts/" + userForOperation.getId())
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(PLUS_OPERATION)))
                .andDo(print())
                .andExpect(status().isOk());
        this.mockMvc.perform(patch("/admin/bonus_accounts/" + userForOperation.getId())
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(PLUS_OPERATION)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.exception")
                        .value("ru.sberbank.bonus_points_system.exception.DoubleOperationException"));
        val processingAcc = bonusAccountRepository.findByUser(userForOperation).get();
        assertEquals(processingAcc.getBonus(), OPERATOR_BONUS_ACC.getBonus().add(PLUS_OPERATION.getChange()));
        assertEquals(processingAcc.getVersion(), OPERATOR_BONUS_ACC.getVersion() + 1);
    }


}
