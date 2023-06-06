-- noinspection SqlWithoutWhereForFile

DELETE
FROM user_roles;
DELETE
FROM operations;
DELETE
FROM bonus_accounts;
DELETE
FROM service_users;

INSERT INTO service_users (email, enabled, login, name, password, registered)
VALUES ('ivan@gmail.com', true, 'admin', 'Иван',
        '{bcrypt}$2a$12$S/AoaSTWSIUs7F9kJQ22yeAVJzrNmpHQNYC2pckB/9yjiLugCfQRG', '2023-06-01'),
       ('lida@gmail.com', true, 'operator', 'Лидия',
        '{bcrypt}$2a$11$EiKhJiGVRLvzoi4NYcchDugY.nwPvEczXp5NIjTstQCPHqQ2M4gLO', '2023-06-02'),
       ('evgeniy@gmail.com', true, 'user', 'Евгений',
        '{bcrypt}$2a$11$rt9m9QkEN.bnyAmZfglSCumXVlQpuKyKnVngT.WPzO.7L5RtldrqC', '2023-06-03');

INSERT INTO user_roles (user_id, role)
VALUES (1, 'ADMIN'),
       (1, 'OPERATOR'),
       (1, 'USER'),
       (2, 'OPERATOR'),
       (2, 'USER'),
       (3, 'USER');

INSERT INTO bonus_accounts (bonus, last_update, version, user_id)
VALUES (522.00, '2023-06-05 00:00:00+00', 8, 1),
       (204.00, '2023-06-06 12:30:00+00', 3, 2);

INSERT INTO operations (change, operation_time, description, external_id, bonus_accounts)
VALUES (500.00, '2023-06-02 00:00:00+00', 'Deposit', 'AUI398', 1),
       (-10.00, '2023-06-01 00:00:00+00', 'Purchase', 'ABC123', 1),
       (10.00, '2023-06-03 11:30:00+00', 'Refund', 'DEF456', 1),
       (-30.25, '2023-06-03 18:45:00+00', 'Purchase', 'GHI789', 1),
       (-15.50, '2023-06-04 00:00:00+00', 'Purchase', 'JKL012', 1),
       (100.00, '2023-06-04 12:30:00+00', 'Deposit', 'MNO345', 1),
       (-7.25, '2023-06-04 18:45:00+00', 'Purchase', 'PQR678', 1),
       (-25.00, '2023-06-05 00:00:00+00', 'Purchase', 'STU901', 1),
       (250.00, '2023-06-04 12:30:00+00', 'Deposit', 'VWX234', 2),
       (-10.50, '2023-06-05 18:45:00+00', 'Purchase', 'YZA567', 2),
       (-35.50, '2023-06-06 12:30:00+00', 'Purchase', 'BCD890', 2);