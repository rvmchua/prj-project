-- ============================================================
-- File: schema.sql
-- Description: Database structure and constraints for banking_app
-- ============================================================

CREATE DATABASE IF NOT EXISTS `banking_app`;
USE `banking_app`;

-- Drop existing tables to ensure clean execution
DROP TABLE IF EXISTS `transactions`;
DROP TABLE IF EXISTS `accounts`;

-- ------------------------------------------------------------
-- Table Structure: accounts
-- ------------------------------------------------------------
CREATE TABLE `accounts` (
                            `entry_id`       INT(11)        NOT NULL AUTO_INCREMENT,
                            `account_number` BIGINT(20)     NOT NULL,
                            `first_name`     VARCHAR(50)    NOT NULL,
                            `last_name`      VARCHAR(50)    NOT NULL,
                            `pin_code`       BIGINT(6)      NOT NULL,
                            `balance`        DECIMAL(15,2)  NOT NULL DEFAULT 0.00,
                            `created_at`     DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `updated_at`     DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            PRIMARY KEY (`entry_id`),
                            UNIQUE KEY `uk_account_number` (`account_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------------------------------------
-- Table Structure: transactions
-- ------------------------------------------------------------
CREATE TABLE `transactions` (
                                `transaction_id` BIGINT(20)     NOT NULL AUTO_INCREMENT,
                                `account_number` BIGINT(20)     NOT NULL,
                                `type`           ENUM('WITHDRAW','DEPOSIT','TRANSFER') NOT NULL,
                                `amount`         DECIMAL(15,2)  NOT NULL,
                                `balance_after`  DECIMAL(15,2)  NOT NULL,
                                `created_at`     DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                PRIMARY KEY (`transaction_id`),
                                KEY `idx_account_number` (`account_number`),
                                CONSTRAINT `fk_transactions_accounts`
                                    FOREIGN KEY (`account_number`)
                                        REFERENCES `accounts` (`account_number`)
                                        ON DELETE CASCADE
                                        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;