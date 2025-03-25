-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS gestion_motocicletas;

-- Usar la base de datos
USE gestion_motocicletas;

-- Tabla de motocicletas
CREATE TABLE IF NOT EXISTS `motocicletas` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `marca` varchar(50) NOT NULL,
  `cilindraje` int(11) NOT NULL,
  `precio` decimal(10,2) NOT NULL,
  `color` varchar(30) NOT NULL,
  `fecha_registro` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS `usuarios` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `rol` enum('admin','usuario') NOT NULL,
  `nombre` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;


-- Insertar usuario administrador
INSERT INTO `usuarios` (`username`, `password`, `rol`, `nombre`, `email`) VALUES
('admin', '$2a$10$XURPShQNCsLjp1ESc2laoObo9QZDhxz73hJPaEv7/cBha4pk0AgP.', 'admin', 'Administrador', 'admin@moto.com')
ON DUPLICATE KEY UPDATE `username` = `username`;

-- Aquí puedes añadir datos de ejemplo para motocicletas si lo deseas