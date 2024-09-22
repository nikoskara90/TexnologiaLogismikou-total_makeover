-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Εξυπηρετητής: 127.0.0.1
-- Χρόνος δημιουργίας: 21 Σεπ 2024 στις 14:10:12
-- Έκδοση διακομιστή: 10.4.27-MariaDB
-- Έκδοση PHP: 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Βάση δεδομένων: `project_makeover`
--

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `conference`
--

CREATE TABLE `conference` (
  `conference_id` bigint(20) NOT NULL,
  `creation_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `name` varchar(255) NOT NULL,
  `description` text NOT NULL,
  `state` enum('CREATED','SUBMISSION','ASSIGNMENT','REVIEW','DECISION','FINAL_SUBMISSION','FINAL') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Άδειασμα δεδομένων του πίνακα `conference`
--

INSERT INTO `conference` (`conference_id`, `creation_date`, `name`, `description`, `state`) VALUES
(1, '2024-09-06 15:18:28', 'confernece1', 'sample', 'CREATED'),
(23, '2024-09-07 11:17:33', 'Kellychester', 'Profound tertiary groupware', 'CREATED'),
(24, '2024-09-07 11:31:55', 'South Alanamouth', 'Enterprise-wide human-resource focus group', 'CREATED'),
(26, '2024-09-08 12:42:48', 'North Araport', 'Persevering discrete approach', 'FINAL_SUBMISSION'),
(27, '2024-09-08 14:55:03', 'Dickinsonfort', 'Synergistic mission-critical customer loyalty', 'FINAL'),
(28, '2024-09-08 14:58:24', 'Port Jabariberg', 'Distributed radical hub', 'SUBMISSION'),
(29, '2024-09-19 16:04:59', 'Willborough', 'Vision-oriented optimizing standardization', 'CREATED'),
(30, '2024-09-19 16:11:43', 'Lake Brittany', 'Organic exuding paradigm', 'CREATED'),
(33, '2024-09-20 09:46:22', 'Nadiachester', 'Progressive demand-driven capability', 'CREATED'),
(35, '2024-09-20 10:11:55', 'Laurineborough', 'Visionary system-worthy pricing structure', 'CREATED');

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `conference_papers`
--

CREATE TABLE `conference_papers` (
  `conference_id` bigint(20) NOT NULL,
  `paper_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Άδειασμα δεδομένων του πίνακα `conference_papers`
--

INSERT INTO `conference_papers` (`conference_id`, `paper_id`) VALUES
(35, 4),
(35, 5);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `conference_pc_chair`
--

CREATE TABLE `conference_pc_chair` (
  `conference_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Άδειασμα δεδομένων του πίνακα `conference_pc_chair`
--

INSERT INTO `conference_pc_chair` (`conference_id`, `user_id`) VALUES
(28, 4),
(35, 1),
(35, 2);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `conference_pc_members`
--

CREATE TABLE `conference_pc_members` (
  `conference_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Άδειασμα δεδομένων του πίνακα `conference_pc_members`
--

INSERT INTO `conference_pc_members` (`conference_id`, `user_id`) VALUES
(28, 1),
(35, 2),
(35, 4);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `paper`
--

CREATE TABLE `paper` (
  `paper_id` bigint(20) NOT NULL,
  `creation_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `title` varchar(255) NOT NULL,
  `abstract_text` varchar(255) DEFAULT NULL,
  `content` text DEFAULT NULL,
  `author_names` varchar(255) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `reviewer1_id` bigint(20) DEFAULT NULL,
  `reviewer2_id` bigint(20) DEFAULT NULL,
  `reviewer1_comments` text DEFAULT NULL,
  `reviewer2_comments` text DEFAULT NULL,
  `reviewer1_score` int(11) DEFAULT NULL,
  `reviewer2_score` int(11) DEFAULT NULL,
  `state` enum('CREATED','SUBMITTED','REVIEWED','REJECTED','APPROVED','ACCEPTED') DEFAULT 'CREATED',
  `conference_id` bigint(20) DEFAULT NULL,
  `submitted` tinyint(1) DEFAULT NULL,
  `needs_modification` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Άδειασμα δεδομένων του πίνακα `paper`
--

INSERT INTO `paper` (`paper_id`, `creation_date`, `title`, `abstract_text`, `content`, `author_names`, `user_id`, `reviewer1_id`, `reviewer2_id`, `reviewer1_comments`, `reviewer2_comments`, `reviewer1_score`, `reviewer2_score`, `state`, `conference_id`, `submitted`, `needs_modification`) VALUES
(1, '2024-09-06 15:23:27', 'Regional Accounts Planner', 'sample', 'content', 'Nikos karagiannis', 1, NULL, NULL, NULL, NULL, NULL, NULL, 'CREATED', 28, 0, 0),
(2, '2024-09-06 15:41:40', 'New Paper Title', NULL, 'Updated content of the paper', 'Nikos karagiannis,Swtiris Flaskis', 1, NULL, NULL, NULL, NULL, NULL, NULL, 'CREATED', 28, 1, 0),
(3, '2024-09-07 17:08:01', 'International Mobility Executive', 'sample', 'content', 'Nikos karagiannis', 1, NULL, NULL, NULL, NULL, NULL, NULL, 'CREATED', 1, 0, 0),
(4, '2024-09-07 17:42:48', 'Central Security Executive', 'sample', 'content', 'Nikos karagiannis', 1, NULL, NULL, NULL, NULL, NULL, NULL, 'CREATED', 35, 0, 0),
(5, '2024-09-07 17:46:52', 'Internal Data Representative', 'sample', 'content', 'Nikos karagiannis', 1, NULL, NULL, NULL, NULL, NULL, NULL, 'CREATED', 35, 0, 0),
(6, '2024-09-07 17:51:26', 'Personal Loan Account', 'Updated abstract content', 'Updated content of the paper', 'Nikos karagiannis,Swtiris Flaskis', 1, NULL, NULL, NULL, NULL, NULL, NULL, 'CREATED', 1, 0, 0),
(7, '2024-09-08 14:20:09', 'Internal Web Coordinator', 'sample', 'content', 'Nikos karagiannis', 1, NULL, NULL, NULL, NULL, NULL, NULL, 'CREATED', 1, 0, 0),
(8, '2024-09-09 11:02:15', 'District Identity Orchestrator', 'sample', 'content', 'Nikos karagiannis', 1, NULL, NULL, NULL, NULL, NULL, NULL, 'CREATED', 1, 0, 0),
(10, '2024-09-09 11:07:33', 'District Tactics Executive', 'sample', 'content', 'Nikos karagiannis', 1, 4, NULL, 'Est est et repudiandae saepe suscipit.', NULL, 5, NULL, 'APPROVED', 28, 1, 1),
(11, '2024-09-10 10:12:14', 'Senior Brand Strategist', 'sample', 'content', 'Nikos karagiannis', 1, NULL, NULL, NULL, NULL, NULL, NULL, 'REJECTED', 26, 0, 0),
(12, '2024-09-19 16:35:36', 'Human Branding Technician', 'sample', 'content', 'Nikos karagiannis', 1, NULL, NULL, NULL, NULL, NULL, NULL, 'CREATED', 26, 0, 0),
(13, '2024-09-19 16:43:21', 'Customer Accountability Assistant', 'sample', 'content', 'Nikos karagiannis', 1, NULL, NULL, NULL, NULL, NULL, NULL, 'CREATED', 26, 0, 0),
(14, '2024-09-19 16:50:36', 'Internal Infrastructure Analyst', 'sample', 'modified content', 'Nikos karagiannis', 1, 1, 4, 'Sint nihil et.', 'Laudantium incidunt quibusdam.', 6, 7, 'CREATED', 26, 0, 0);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `paper_coauthors`
--

CREATE TABLE `paper_coauthors` (
  `user_id` bigint(20) NOT NULL,
  `paper_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Άδειασμα δεδομένων του πίνακα `paper_coauthors`
--

INSERT INTO `paper_coauthors` (`user_id`, `paper_id`) VALUES
(1, 1),
(1, 2),
(2, 1),
(2, 2);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `role`
--

CREATE TABLE `role` (
  `role_id` int(11) NOT NULL,
  `role_name` enum('VISITOR','AUTHOR','PC_MEMBER','PC_CHAIR') NOT NULL,
  `user_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Άδειασμα δεδομένων του πίνακα `role`
--

INSERT INTO `role` (`role_id`, `role_name`, `user_id`) VALUES
(1, 'AUTHOR', 1),
(21, 'PC_MEMBER', 4),
(22, 'AUTHOR', 4),
(23, 'VISITOR', 4),
(26, 'PC_CHAIR', 2),
(29, 'PC_MEMBER', 2),
(30, 'AUTHOR', 2),
(32, 'PC_MEMBER', 1),
(33, 'VISITOR', 1),
(37, 'PC_CHAIR', 1),
(39, 'VISITOR', 9);

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `user`
--

CREATE TABLE `user` (
  `user_id` bigint(20) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `fullname` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Άδειασμα δεδομένων του πίνακα `user`
--

INSERT INTO `user` (`user_id`, `username`, `password`, `fullname`) VALUES
(1, 'kara', '1234', 'Nikolaos Karagiannis'),
(2, 'swtos', '12345', 'swtiris flaskis'),
(3, 'Ross0', 'oJtZaWzkS6t_lzw', 'Kathryn Veum'),
(4, 'Dell_Baumbach', '6cd2qF5NKCRoGUB', 'Jacquelyn Huels'),
(5, 'Verona.Goyette24', 'Vv6naUfkc3jdJW3', 'Floyd Hickle'),
(6, 'Aidan.Tromp', 'GbYsCTX2OYuT13N', 'Kellie McDermott'),
(7, 'Coleman.Brekke8', '8F8BXZNMrfaiYMz', 'Yolanda Bashirian'),
(8, 'Donny4', 'igMsfEXasdFUH6j', 'Vera Borer'),
(9, 'Ayla7', 'Q1AXDp_wZhXqE6Z', 'Milton Runte');

--
-- Ευρετήρια για άχρηστους πίνακες
--

--
-- Ευρετήρια για πίνακα `conference`
--
ALTER TABLE `conference`
  ADD PRIMARY KEY (`conference_id`),
  ADD UNIQUE KEY `name` (`name`),
  ADD UNIQUE KEY `unique_description` (`description`) USING HASH;

--
-- Ευρετήρια για πίνακα `conference_papers`
--
ALTER TABLE `conference_papers`
  ADD PRIMARY KEY (`conference_id`,`paper_id`),
  ADD KEY `paper_id` (`paper_id`);

--
-- Ευρετήρια για πίνακα `conference_pc_chair`
--
ALTER TABLE `conference_pc_chair`
  ADD PRIMARY KEY (`conference_id`,`user_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Ευρετήρια για πίνακα `conference_pc_members`
--
ALTER TABLE `conference_pc_members`
  ADD PRIMARY KEY (`conference_id`,`user_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Ευρετήρια για πίνακα `paper`
--
ALTER TABLE `paper`
  ADD PRIMARY KEY (`paper_id`),
  ADD UNIQUE KEY `title` (`title`),
  ADD KEY `fk_user_id` (`user_id`),
  ADD KEY `fk_reviewer1_user` (`reviewer1_id`),
  ADD KEY `fk_reviewer2_user` (`reviewer2_id`);

--
-- Ευρετήρια για πίνακα `paper_coauthors`
--
ALTER TABLE `paper_coauthors`
  ADD PRIMARY KEY (`user_id`,`paper_id`),
  ADD KEY `paper_id` (`paper_id`);

--
-- Ευρετήρια για πίνακα `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`role_id`),
  ADD KEY `fk_user_role` (`user_id`);

--
-- Ευρετήρια για πίνακα `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT για άχρηστους πίνακες
--

--
-- AUTO_INCREMENT για πίνακα `conference`
--
ALTER TABLE `conference`
  MODIFY `conference_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=36;

--
-- AUTO_INCREMENT για πίνακα `paper`
--
ALTER TABLE `paper`
  MODIFY `paper_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT για πίνακα `role`
--
ALTER TABLE `role`
  MODIFY `role_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;

--
-- AUTO_INCREMENT για πίνακα `user`
--
ALTER TABLE `user`
  MODIFY `user_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- Περιορισμοί για άχρηστους πίνακες
--

--
-- Περιορισμοί για πίνακα `conference_papers`
--
ALTER TABLE `conference_papers`
  ADD CONSTRAINT `conference_papers_ibfk_1` FOREIGN KEY (`conference_id`) REFERENCES `conference` (`conference_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `conference_papers_ibfk_2` FOREIGN KEY (`paper_id`) REFERENCES `paper` (`paper_id`) ON DELETE CASCADE;

--
-- Περιορισμοί για πίνακα `conference_pc_chair`
--
ALTER TABLE `conference_pc_chair`
  ADD CONSTRAINT `conference_pc_chair_ibfk_1` FOREIGN KEY (`conference_id`) REFERENCES `conference` (`conference_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `conference_pc_chair_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_conference_pc_chair_conference_id` FOREIGN KEY (`conference_id`) REFERENCES `conference` (`conference_id`) ON DELETE CASCADE;

--
-- Περιορισμοί για πίνακα `conference_pc_members`
--
ALTER TABLE `conference_pc_members`
  ADD CONSTRAINT `conference_pc_members_ibfk_1` FOREIGN KEY (`conference_id`) REFERENCES `conference` (`conference_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `conference_pc_members_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_conference_pc_members_conference_id` FOREIGN KEY (`conference_id`) REFERENCES `conference` (`conference_id`) ON DELETE CASCADE;

--
-- Περιορισμοί για πίνακα `paper_coauthors`
--
ALTER TABLE `paper_coauthors`
  ADD CONSTRAINT `paper_coauthors_ibfk_1` FOREIGN KEY (`paper_id`) REFERENCES `paper` (`paper_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `paper_coauthors_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE;

--
-- Περιορισμοί για πίνακα `role`
--
ALTER TABLE `role`
  ADD CONSTRAINT `fk_user_role` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
