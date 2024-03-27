-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : mer. 27 mars 2024 à 08:40
-- Version du serveur : 8.0.31
-- Version de PHP : 8.0.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `projet_bibliotheque`
--
DROP DATABASE IF EXISTS projet_bibliotheque;
CREATE DATABASE projet_bibliotheque ;
USE projet_bibliotheque ;

-- --------------------------------------------------------

--
-- Structure de la table `auteur`
--

DROP TABLE IF EXISTS `auteur`;
CREATE TABLE IF NOT EXISTS `auteur` (
  `idAuteur` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `prenom` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `dateNaissance` date NOT NULL,
  `nationalite` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`idAuteur`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `auteur`
--

INSERT INTO `auteur` (`idAuteur`, `nom`, `prenom`, `dateNaissance`, `nationalite`) VALUES
(1, 'Martin', 'George R.R.', '1948-09-20', 'Américain'),
(2, 'Rowling', 'J.K.', '1965-07-31', 'Britannique'),
(3, 'Tolkien', 'J.R.R.', '1892-01-03', 'Britannique'),
(4, 'King', 'Stephen', '1947-09-21', 'Américain'),
(5, 'Brown', 'Dan', '1964-06-22', 'Américain'),
(6, 'Saint-Exupéry', 'Antoine de', '1900-06-29', 'Français'),
(7, 'Camus', 'Albert', '1913-11-07', 'Français'),
(8, 'Hugo', 'Victor', '1802-02-26', 'Français'),
(9, 'Coelho', 'Paulo', '1947-08-24', 'Brésilien'),
(10, 'Martin', 'George R.R.', '1948-09-20', 'Américain'),
(11, 'Rowling', 'J.K.', '1965-07-31', 'Britannique'),
(12, 'Tolkien', 'J.R.R.', '1892-01-03', 'Britannique'),
(13, 'King', 'Stephen', '1947-09-21', 'Américain'),
(14, 'Brown', 'Dan', '1964-06-22', 'Américain'),
(15, 'Saint-Exupéry', 'Antoine de', '1900-06-29', 'Français'),
(16, 'Camus', 'Albert', '1913-11-07', 'Français'),
(17, 'Hugo', 'Victor', '1802-02-26', 'Français'),
(18, 'Coelho', 'Paulo', '1947-08-24', 'Brésilien');

-- --------------------------------------------------------

--
-- Structure de la table `emprunter`
--

DROP TABLE IF EXISTS `emprunter`;
CREATE TABLE IF NOT EXISTS `emprunter` (
  `ISBN` varchar(13) COLLATE utf8mb4_general_ci NOT NULL,
  `idUser` int NOT NULL,
  `dateEmprunt` date NOT NULL,
  `dateRerour` date NOT NULL,
  PRIMARY KEY (`ISBN`,`idUser`,`dateEmprunt`),
  KEY `Emprunter_Users1_FK` (`idUser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `livre`
--

DROP TABLE IF EXISTS `livre`;
CREATE TABLE IF NOT EXISTS `livre` (
  `ISBN` varchar(13) COLLATE utf8mb4_general_ci NOT NULL,
  `titre` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `editeur` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `genre` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `nbPage` int NOT NULL,
  `langue` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `datePublication` date NOT NULL,
  `description` varchar(500) COLLATE utf8mb4_general_ci NOT NULL,
  `idAuteur` int NOT NULL,
  `IdStock` int NOT NULL,
  PRIMARY KEY (`ISBN`),
  UNIQUE KEY `Livre_Stock0_AK` (`IdStock`),
  KEY `Livre_Auteur0_FK` (`idAuteur`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `livre`
--

INSERT INTO `livre` (`ISBN`, `titre`, `editeur`, `genre`, `nbPage`, `langue`, `datePublication`, `description`, `idAuteur`, `IdStock`) VALUES
('9780544003415', 'Harry Potter à l\'école des sorciers', 'Gallimard', 'Fantasy', 320, 'Français', '1998-10-26', 'Premier livre de la série Harry Potter', 2, 1),
('9780548132088', 'The Lord of the Rings', 'Allen & Unwin', 'Fantasy', 1178, 'Anglais', '1954-07-29', 'Roman de fantasy épique', 3, 2),
('9780553103540', 'A Game of Thrones', 'Bantam Spectra', 'Fantasy', 694, 'Anglais', '1996-08-06', 'Premier livre dans la série A Song of Ice and Fire', 1, 4),
('9780671027346', 'Angels & Demons', 'Pocket Books', 'Thriller', 572, 'Anglais', '2000-05-23', 'Préquelle de The Da Vinci Code', 5, 6),
('9781400033416', 'The Da Vinci Code', 'Doubleday', 'Mystère', 454, 'Anglais', '2003-03-18', 'Roman de Dan Brown', 5, 5),
('9781501142970', 'It', 'Viking', 'Horreur', 1138, 'Anglais', '1986-09-15', 'Roman de Stephen King', 4, 3),
('9782070423205', 'Le Petit Prince', 'Gallimard', 'Conte philosophique', 96, 'Français', '1943-04-06', 'Chef-d\'œuvre de la littérature française', 6, 7),
('9782070726052', 'L\'Étranger', 'Gallimard', 'Roman', 123, 'Français', '1942-06-01', 'Roman d\'Albert Camus', 7, 8),
('9782080700341', 'L\'Alchimiste', 'Flammarion', 'Roman initiatique', 218, 'Français', '1988-01-01', 'Roman de Paulo Coelho', 9, 10),
('9782211064298', 'Les Misérables', 'Garnier-Flammarion', 'Roman historique', 1952, 'Français', '1862-01-01', 'Roman de Victor Hugo', 8, 9);

-- --------------------------------------------------------

--
-- Structure de la table `noter`
--

DROP TABLE IF EXISTS `noter`;
CREATE TABLE IF NOT EXISTS `noter` (
  `ISBN` varchar(13) COLLATE utf8mb4_general_ci NOT NULL,
  `idUser` int NOT NULL,
  `note` int NOT NULL,
  `commentaire` varchar(250) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`ISBN`,`idUser`),
  KEY `Noter_Users1_FK` (`idUser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `penaliter`
--

DROP TABLE IF EXISTS `penaliter`;
CREATE TABLE IF NOT EXISTS `penaliter` (
  `ISBN` varchar(13) COLLATE utf8mb4_general_ci NOT NULL,
  `idUser` int NOT NULL,
  `datePenalite` date NOT NULL,
  PRIMARY KEY (`ISBN`,`idUser`),
  KEY `Penaliter_Users1_FK` (`idUser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `stock`
--

DROP TABLE IF EXISTS `stock`;
CREATE TABLE IF NOT EXISTS `stock` (
  `IdStock` int NOT NULL AUTO_INCREMENT,
  `nbTotal` int NOT NULL,
  `nbDisponible` int NOT NULL,
  `ISBN` varchar(13) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`IdStock`),
  UNIQUE KEY `Stock_Livre0_AK` (`ISBN`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `stock`
--

INSERT INTO `stock` (`IdStock`, `nbTotal`, `nbDisponible`, `ISBN`) VALUES
(1, 10, 10, '9780553103540'),
(2, 5, 5, '9781400033416'),
(3, 1, 1, '9780671027346'),
(4, 7, 7, '9780544003415'),
(5, 3, 3, '9780548132088'),
(6, 4, 4, '9781501142970'),
(7, 2, 2, '9782070423205'),
(8, 3, 3, '9782070726052'),
(9, 6, 6, '9782211064298'),
(10, 3, 3, '9782080700341');

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `idUser` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `prenom` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(250) COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(250) COLLATE utf8mb4_general_ci NOT NULL,
  `role` tinyint(1) NOT NULL,
  `createdDate` datetime NOT NULL,
  `updatedDate` datetime NOT NULL,
  PRIMARY KEY (`idUser`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `users`
--

INSERT INTO `users` (`idUser`, `nom`, `prenom`, `email`, `password`, `role`, `createdDate`, `updatedDate`) VALUES
(1, 'Fernandes', 'Joel', 'joel.fernandes@client.fr', 'joel', 0, '2024-03-27 08:33:21', '2024-03-27 08:33:21'),
(2, 'Bodelot', 'Mathis', 'mathis.bodelot@client.fr', 'mathis', 0, '2024-03-27 08:33:21', '2024-03-27 08:33:21'),
(3, 'Bresson', 'Luc', 'admin@admin.fr', 'admin', 1, '2024-03-27 08:36:01', '2024-03-27 08:36:01');

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `emprunter`
--
ALTER TABLE `emprunter`
  ADD CONSTRAINT `Emprunter_Livre0_FK` FOREIGN KEY (`ISBN`) REFERENCES `livre` (`ISBN`),
  ADD CONSTRAINT `Emprunter_Users1_FK` FOREIGN KEY (`idUser`) REFERENCES `users` (`idUser`);

--
-- Contraintes pour la table `livre`
--
ALTER TABLE `livre`
  ADD CONSTRAINT `Livre_Auteur0_FK` FOREIGN KEY (`idAuteur`) REFERENCES `auteur` (`idAuteur`),
  ADD CONSTRAINT `Livre_Stock1_FK` FOREIGN KEY (`IdStock`) REFERENCES `stock` (`IdStock`);

--
-- Contraintes pour la table `noter`
--
ALTER TABLE `noter`
  ADD CONSTRAINT `Noter_Livre0_FK` FOREIGN KEY (`ISBN`) REFERENCES `livre` (`ISBN`),
  ADD CONSTRAINT `Noter_Users1_FK` FOREIGN KEY (`idUser`) REFERENCES `users` (`idUser`);

--
-- Contraintes pour la table `penaliter`
--
ALTER TABLE `penaliter`
  ADD CONSTRAINT `Penaliter_Livre0_FK` FOREIGN KEY (`ISBN`) REFERENCES `livre` (`ISBN`),
  ADD CONSTRAINT `Penaliter_Users1_FK` FOREIGN KEY (`idUser`) REFERENCES `users` (`idUser`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
