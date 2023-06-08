package ru.sberbank.bonus_points_system;

import jakarta.transaction.Transactional;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.sberbank.bonus_points_system.mapper.BonusAccountMapper;
import ru.sberbank.bonus_points_system.mapper.BonusOperationMapper;
import ru.sberbank.bonus_points_system.mapper.UserMapper;
import ru.sberbank.bonus_points_system.repository.BonusAccountRepository;
import ru.sberbank.bonus_points_system.repository.BonusOperationRepository;
import ru.sberbank.bonus_points_system.repository.UserRepository;
import ru.sberbank.bonus_points_system.security.dto.JwtRequest;
import ru.sberbank.bonus_points_system.security.dto.JwtResponse;
import ru.sberbank.bonus_points_system.test_util.JsonUtil;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.sberbank.bonus_points_system.test_util.TestData.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class BonusPointsSystemApplicationTests {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected BonusAccountRepository bonusAccountRepository;
    @Autowired
    protected BonusOperationRepository bonusOperationRepository;
    @Autowired
    protected UserMapper userMapper;
    @Autowired
    protected BonusAccountMapper bonusAccountMapper;
    @Autowired
    protected BonusOperationMapper bonusOperationMapper;

    protected static String accessToken;
    protected static String refreshToken;

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    void setup() {
        userRepository.save(ADMIN);
        userRepository.save(OPERATOR);
        userRepository.save(USER);
        var adminBonusAcc = ADMIN_BONUS_ACC;
        adminBonusAcc.setUser(userRepository.findByLogin("admin").get());
        var operatorBonusAcc = OPERATOR_BONUS_ACC;
        operatorBonusAcc.setUser(userRepository.findByLogin("operator").get());
        adminBonusAcc = bonusAccountRepository.save(adminBonusAcc);
        operatorBonusAcc = bonusAccountRepository.save(operatorBonusAcc);
        bonusOperationRepository.save(setBonusAccAndReturn(ADMIN_OPERATION_1, adminBonusAcc));
        bonusOperationRepository.save(setBonusAccAndReturn(ADMIN_OPERATION_2, adminBonusAcc));
        bonusOperationRepository.save(setBonusAccAndReturn(ADMIN_OPERATION_3, adminBonusAcc));
        bonusOperationRepository.save(setBonusAccAndReturn(ADMIN_OPERATION_4, adminBonusAcc));
        bonusOperationRepository.save(setBonusAccAndReturn(ADMIN_OPERATION_5, adminBonusAcc));
        bonusOperationRepository.save(setBonusAccAndReturn(ADMIN_OPERATION_6, adminBonusAcc));
        bonusOperationRepository.save(setBonusAccAndReturn(ADMIN_OPERATION_7, adminBonusAcc));
        bonusOperationRepository.save(setBonusAccAndReturn(ADMIN_OPERATION_8, adminBonusAcc));
        bonusOperationRepository.save(setBonusAccAndReturn(OPERATOR_OPERATION_1, operatorBonusAcc));
        bonusOperationRepository.save(setBonusAccAndReturn(OPERATOR_OPERATION_2, operatorBonusAcc));
        bonusOperationRepository.save(setBonusAccAndReturn(OPERATOR_OPERATION_3, operatorBonusAcc));
    }

    void login(JwtRequest jwtRequest) {
        try {
            val resultActions = this.mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtil.writeValue(jwtRequest)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
            val json = resultActions.andReturn().getResponse().getContentAsString();
            val tokens = JsonUtil.readValue(json, JwtResponse.class);
            accessToken = tokens.getAccessToken();
            refreshToken = tokens.getRefreshToken();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
