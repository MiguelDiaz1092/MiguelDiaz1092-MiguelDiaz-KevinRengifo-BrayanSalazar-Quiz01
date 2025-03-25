package com.gestionmotos.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase singleton para gestionar la conexión a la base de datos.
 */
public class DatabaseConnection {
    // Propiedades para la conexión
    private String url;
    private String user;
    private String password;
    
    // Instancia única
    private static DatabaseConnection instance;
    
    // Conexión a la base de datos
    private Connection connection;
    
    /**
     * Constructor privado para implementar el patrón Singleton.
     * Carga las propiedades de conexión desde el archivo de configuración.
     */
    private DatabaseConnection() {
        loadProperties();
        
        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error al cargar el driver de MySQL: " + e.getMessage());
            throw new RuntimeException("No se pudo cargar el driver de MySQL", e);
        }
    }
    
    /**
     * Carga las propiedades de conexión desde el archivo de configuración.
     */
    private void loadProperties() {
        Properties properties = new Properties();
        
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.err.println("No se pudo encontrar el archivo config.properties");
                // Usar valores por defecto
                url = "jdbc:mysql://localhost:3306/gestion_motocicletas";
                user = "root";
                password = "";
                return;
            }
            
            // Cargar el archivo de propiedades
            properties.load(input);
            
            // Obtener las propiedades
            url = properties.getProperty("db.url");
            user = properties.getProperty("db.user");
            password = properties.getProperty("db.password");
            
        } catch (IOException e) {
            System.err.println("Error al cargar el archivo de propiedades: " + e.getMessage());
            e.printStackTrace();
            // Usar valores por defecto
            url = "jdbc:mysql://localhost:3306/gestion_motocicletas";
            user = "root";
            password = "";
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
            connection = DriverManager.getConnection(url, user, password);
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