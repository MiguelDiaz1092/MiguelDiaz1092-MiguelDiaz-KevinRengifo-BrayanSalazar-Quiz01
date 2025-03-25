package com.gestionmotos.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase singleton para gestionar la conexión a la base de datos.
 */
public class DatabaseConnection {
    // Configuración de la base de datos
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_motocicletas";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Sin contraseña para XAMPP por defecto
    
    // Instancia única
    private static DatabaseConnection instance;
    
    // Conexión a la base de datos
    private Connection connection;
    
    /**
     * Constructor privado para implementar el patrón Singleton.
     */
    private DatabaseConnection() {
        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error al cargar el driver de MySQL: " + e.getMessage());
            throw new RuntimeException("No se pudo cargar el driver de MySQL", e);
        }
    }
    
    /**
     * Obtiene la instancia única de la clase (Singleton).
     * @return La instancia de DatabaseConnection
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    /**
     * Establece una conexión con la base de datos.
     * @return Una conexión a la base de datos
     * @throws SQLException Si ocurre un error al conectar
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
    
    /**
     * Cierra la conexión a la base de datos.
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}