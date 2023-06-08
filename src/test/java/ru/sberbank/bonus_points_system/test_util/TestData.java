package ru.sberbank.bonus_points_system.test_util;

import ru.sberbank.bonus_points_system.dao.BonusAccount;
import ru.sberbank.bonus_points_system.dao.BonusOperation;
import ru.sberbank.bonus_points_system.dao.Role;
import ru.sberbank.bonus_points_system.dao.User;
import ru.sberbank.bonus_points_system.dto.BonusOperationDto;
import ru.sberbank.bonus_points_system.dto.UserDto;
import ru.sberbank.bonus_points_system.security.dto.JwtRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

public class TestData {

    public static final String INVALID_JWT_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTY4NjIwNjE1N" +
            "ywicm9sZXMiOlsiT1BFUkFUT1IiLCJVU0VSIiwiINVALIDiXSwibmFtZSI6ItCY0LLQsNC9IiwiaWQiOjF9.S-64on1lgxNaAOb0U2" +
            "vPtuoE1197N_gTyB4Tcp5BX8wJbfnScqLYQYQvaOgQhrqJgStNT5ZS81TZ44Imh1kWgw";

    public static final JwtRequest ADMIN_JWT_REQUEST = new JwtRequest("admin", "admin56");
    public static final JwtRequest OPERATOR_JWT_REQUEST = new JwtRequest("operator", "pass34");
    public static final JwtRequest USER_JWT_REQUEST = new JwtRequest("user", "pass12");
    public static final JwtRequest NOT_FOUND_JWT_REQUEST = new JwtRequest("nonexistent", "password");
    public static final JwtRequest INCORRECT_PASS_JWT_REQUEST = new JwtRequest("user", "incorrect");

    public static final Set<Role> USER_ROLE_SET = new HashSet<>();
    public static final Set<Role> OPERATOR_ROLE_SET = new HashSet<>();

    static {
        USER_ROLE_SET.add(Role.USER);
        OPERATOR_ROLE_SET.add(Role.USER);
        OPERATOR_ROLE_SET.add(Role.OPERATOR);
    }

    public static final User ADMIN = new User(null, "Ivan", "admin",
            "{bcrypt}$2a$12$S/AoaSTWSIUs7F9kJQ22yeAVJzrNmpHQNYC2pckB/9yjiLugCfQRG", "ivan@gmail.com",
            LocalDate.of(2023, Month.JUNE, 1), true, Role.ADMIN, Role.USER, Role.OPERATOR);
    public static final User OPERATOR = new User(null, "Lidia", "operator",
            "{bcrypt}$2a$11$EiKhJiGVRLvzoi4NYcchDugY.nwPvEczXp5NIjTstQCPHqQ2M4gLO", "lida@gmail.com",
            LocalDate.of(2023, Month.JUNE, 2), true, Role.USER, Role.OPERATOR);
    public static final User USER = new User(null, "Evgeniy", "user",
            "{bcrypt}$2a$11$rt9m9QkEN.bnyAmZfglSCumXVlQpuKyKnVngT.WPzO.7L5RtldrqC", "evgeniy@gmail.com",
            LocalDate.of(2023, Month.JUNE, 3), true, Role.USER);
    public static final UserDto NEW_USER = UserDto.builder()
            .name("Slava")
            .login("newuser")
            .password("newpass99")
            .email("newuser@gmail.com")
            .roles(USER_ROLE_SET)
            .build();

    public static final UserDto NEW_OPERATOR = UserDto.builder()
            .name("Helga")
            .login("newoperator")
            .password("newpass11")
            .email("newoper@gmail.com")
            .roles(OPERATOR_ROLE_SET)
            .build();

    public static final UserDto ALREADY_EXIST_USER = UserDto.builder()
            .name("Evgesha")
            .login("user")
            .password("newpass99")
            .email("newuser@gmail.com")
            .roles(USER_ROLE_SET)
            .build();

    public static final BonusAccount ADMIN_BONUS_ACC = new BonusAccount(null, null, new BigDecimal(522), 8,
            LocalDateTime.of(2023, Month.JUNE, 1, 0, 0), null);
    public static final BonusAccount OPERATOR_BONUS_ACC = new BonusAccount(null, null, new BigDecimal(204), 3,
            LocalDateTime.of(2023, Month.JUNE, 1, 0, 0), null);

    public static final BonusOperation ADMIN_OPERATION_1 = new BonusOperation(null, null, "Deposit",
            LocalDateTime.of(2023, Month.JUNE, 2, 0, 0), new BigDecimal(500), "AUI398");
    public static final BonusOperation ADMIN_OPERATION_2 = new BonusOperation(null, null, "Purchase",
            LocalDateTime.of(2023, Month.JUNE, 2, 8, 0), new BigDecimal(-10), "ABC123");
    public static final BonusOperation ADMIN_OPERATION_3 = new BonusOperation(null, null, "Refund",
            LocalDateTime.of(2023, Month.JUNE, 3, 11, 30), new BigDecimal(10), "DEF456");
    public static final BonusOperation ADMIN_OPERATION_4 = new BonusOperation(null, null, "Purchase",
            LocalDateTime.of(2023, Month.JUNE, 3, 18, 45), BigDecimal.valueOf(-30.25), "GHI789");
    public static final BonusOperation ADMIN_OPERATION_5 = new BonusOperation(null, null, "Purchase",
            LocalDateTime.of(2023, Month.JUNE, 4, 0, 0), BigDecimal.valueOf(-15.50), "JKL012");
    public static final BonusOperation ADMIN_OPERATION_6 = new BonusOperation(null, null, "Deposit",
            LocalDateTime.of(2023, Month.JUNE, 4, 12, 30), new BigDecimal(100), "MNO345");
    public static final BonusOperation ADMIN_OPERATION_7 = new BonusOperation(null, null, "Purchase",
            LocalDateTime.of(2023, Month.JUNE, 4, 18, 45), BigDecimal.valueOf(-7.25), "PQR678");
    public static final BonusOperation ADMIN_OPERATION_8 = new BonusOperation(null, null, "Purchase",
            LocalDateTime.of(2023, Month.JUNE, 5, 0, 0), new BigDecimal(-25), "STU901");
    public static final BonusOperation OPERATOR_OPERATION_1 = new BonusOperation(null, null, "Deposit",
            LocalDateTime.of(2023, Month.JUNE, 4, 12, 30), new BigDecimal(250), "VWX234");
    public static final BonusOperation OPERATOR_OPERATION_2 = new BonusOperation(null, null, "Purchase",
            LocalDateTime.of(2023, Month.JUNE, 5, 18, 45), new BigDecimal(-10), "YZA567");
    public static final BonusOperation OPERATOR_OPERATION_3 = new BonusOperation(null, null, "Purchase",
            LocalDateTime.of(2023, Month.JUNE, 6, 12, 30), new BigDecimal(-36), "BCD890");

    public static final BonusOperationDto PLUS_OPERATION = BonusOperationDto.builder()
            .change(new BigDecimal(100))
            .description("New deposit")
            .externalID("NLK989")
            .build();
    public static final BonusOperationDto MINUS_OPERATION = BonusOperationDto.builder()
            .change(new BigDecimal(-100))
            .description("New purchase")
            .externalID("PPK121")
            .build();
    public static final BonusOperationDto BIG_MINUS_OPERATION = BonusOperationDto.builder()
            .change(new BigDecimal(-1000))
            .description("New big purchase")
            .externalID("BIG666")
            .build();

    public static BonusOperation setBonusAccAndReturn(BonusOperation bonusOperation, BonusAccount bonusAccount) {
        bonusOperation.setAccount(bonusAccount);
        return bonusOperation;
    }

    public static String jsonWithPassword(UserDto user, String password) {
        return JsonUtil.writeAdditionProps(user, "password", password);
    }


}
