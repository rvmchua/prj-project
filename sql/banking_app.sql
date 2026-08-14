-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Aug 14, 2026 at 07:22 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `banking_app`
--

-- --------------------------------------------------------

--
-- Table structure for table `accounts`
--

CREATE TABLE `accounts` (
  `entry_id` int(11) NOT NULL,
  `account_number` bigint(20) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `pin_code` bigint(6) NOT NULL,
  `balance` decimal(15,2) NOT NULL DEFAULT 0.00,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `updated_at` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `accounts`
--

INSERT INTO `accounts` (`entry_id`, `account_number`, `first_name`, `last_name`, `pin_code`, `balance`, `created_at`, `updated_at`) VALUES
(1, 1000001, 'Juan', 'Dela Cruz', 111111, 732.49, '2026-08-12 17:14:30', '2026-08-14 23:33:04'),
(3, 1378886, 'Royce', 'Chua', 1111, 2215.76, '2026-08-12 21:42:50', '2026-08-14 21:01:59'),
(4, 1344391, 'Kimi', 'Chua', 1111, 1336.22, '2026-08-12 21:43:34', '2026-08-14 23:33:04'),
(10, 1107089, 'R', 'C', 1234, 0.00, '2026-08-15 01:06:56', '2026-08-15 01:06:56');

-- --------------------------------------------------------

--
-- Table structure for table `transactions`
--

CREATE TABLE `transactions` (
  `transaction_id` bigint(20) NOT NULL,
  `account_number` bigint(20) NOT NULL,
  `type` enum('WITHDRAW','DEPOSIT','TRANSFER','') NOT NULL,
  `amount` decimal(15,2) NOT NULL,
  `balance_after` decimal(15,2) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transactions`
--

INSERT INTO `transactions` (`transaction_id`, `account_number`, `type`, `amount`, `balance_after`, `created_at`) VALUES
(1, 1000001, 'TRANSFER', -50.00, 584.24, '2026-08-14 22:39:13'),
(2, 1344391, 'TRANSFER', 50.00, 1100.00, '2026-08-14 22:39:13'),
(3, 1000001, 'DEPOSIT', 1000.00, 1584.24, '2026-08-14 23:13:06'),
(4, 1000001, 'DEPOSIT', 10.00, 1594.24, '2026-08-14 23:29:54'),
(5, 1000001, 'DEPOSIT', 50.00, 1644.24, '2026-08-14 23:29:57'),
(6, 1344391, 'WITHDRAW', -5.00, 1095.00, '2026-08-14 23:30:20'),
(7, 1344391, 'WITHDRAW', -20.00, 1075.00, '2026-08-14 23:30:27'),
(8, 1344391, 'WITHDRAW', -75.03, 999.97, '2026-08-14 23:31:09'),
(9, 1344391, 'WITHDRAW', -200.00, 799.97, '2026-08-14 23:31:15'),
(10, 1344391, 'WITHDRAW', -375.50, 424.47, '2026-08-14 23:31:21'),
(11, 1000001, 'TRANSFER', -750.00, 894.24, '2026-08-14 23:32:14'),
(12, 1344391, 'TRANSFER', 750.00, 1174.47, '2026-08-14 23:32:14'),
(13, 1000001, 'TRANSFER', -25.00, 869.24, '2026-08-14 23:32:37'),
(14, 1344391, 'TRANSFER', 25.00, 1199.47, '2026-08-14 23:32:37'),
(15, 1000001, 'TRANSFER', -32.00, 837.24, '2026-08-14 23:32:53'),
(16, 1344391, 'TRANSFER', 32.00, 1231.47, '2026-08-14 23:32:53'),
(17, 1000001, 'TRANSFER', -28.75, 808.49, '2026-08-14 23:32:58'),
(18, 1344391, 'TRANSFER', 28.75, 1260.22, '2026-08-14 23:32:58'),
(19, 1000001, 'TRANSFER', -76.00, 732.49, '2026-08-14 23:33:04'),
(20, 1344391, 'TRANSFER', 76.00, 1336.22, '2026-08-14 23:33:04');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `accounts`
--
ALTER TABLE `accounts`
  ADD PRIMARY KEY (`entry_id`),
  ADD UNIQUE KEY `account_number` (`account_number`);

--
-- Indexes for table `transactions`
--
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`transaction_id`),
  ADD KEY `account_number` (`account_number`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `accounts`
--
ALTER TABLE `accounts`
  MODIFY `entry_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `transactions`
--
ALTER TABLE `transactions`
  MODIFY `transaction_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `transactions`
--
ALTER TABLE `transactions`
  ADD CONSTRAINT `fk_transactions_accounts` FOREIGN KEY (`account_number`) REFERENCES `accounts` (`account_number`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
