package com.gestionmotos.repository;

import com.gestionmotos.model.Usuario;
import com.gestionmotos.util.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del repositorio de usuarios que accede a la base de datos.
 */
public class UsuarioRepositoryImpl implements UsuarioRepository {
    
    /**
     * Guarda un nuevo usuario en la base de datos.
     * @param usuario El usuario a guardar
     * @return El usuario guardado con su ID asignado
     */
    @Override
    public Usuario save(Usuario usuario) {
        String sql = "INSERT INTO usuarios (username, password, rol, nombre, email) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, usuario.getUsername());
            // Encriptar la contraseña antes de guardarla
            String hashedPassword = BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt());
            stmt.setString(2, hashedPassword);
            stmt.setString(3, usuario.getRol());
            stmt.setString(4, usuario.getNombre());
            stmt.setString(5, usuario.getEmail());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Crear usuario falló, ninguna fila afectada.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    usuario.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Crear usuario falló, ningún ID obtenido.");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al guardar usuario: " + e.getMessage());
            e.printStackTrace();
        }
        
        return usuario;
    }
    
    /**
     * Actualiza un usuario existente en la base de datos.
     * @param usuario El usuario a actualizar
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    @Override
    public boolean update(Usuario usuario) {
        String sql = "UPDATE usuarios SET username = ?, rol = ?, nombre = ?, email = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, usuario.getUsername());
            stmt.setString(2, usuario.getRol());
            stmt.setString(3, usuario.getNombre());
            stmt.setString(4, usuario.getEmail());
            stmt.setInt(5, usuario.getId());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Actualiza la contraseña de un usuario.
     * @param id ID del usuario
     * @param newPassword Nueva contraseña (sin encriptar)
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public boolean updatePassword(int id, String newPassword) {
        String sql = "UPDATE usuarios SET password = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            stmt.setString(1, hashedPassword);
            stmt.setInt(2, id);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar contraseña: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Elimina un usuario por su ID.
     * @param id El ID del usuario a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     */
    @Override
    public boolean deleteById(Integer id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Busca un usuario por su ID.
     * @param id El ID del usuario a buscar
     * @return Un Optional que contiene el usuario si existe
     */
    @Override
    public Optional<Usuario> findById(Integer id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("rol"),
                            rs.getString("nombre"),
                            rs.getString("email")
                    );
                    return Optional.of(usuario);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return Optional.empty();
    }
    
    /**
     * Recupera todos los usuarios de la base de datos.
     * @return Una lista con todos los usuarios
     */
    @Override
    public List<Usuario> findAll() {
        String sql = "SELECT * FROM usuarios";
        List<Usuario> usuarios = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("rol"),
                        rs.getString("nombre"),
                        rs.getString("email")
                );
                usuarios.add(usuario);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los usuarios: " + e.getMessage());
            e.printStackTrace();
        }
        
        return usuarios;
    }
    
    /**
     * Busca un usuario por su nombre de usuario.
     * @param username El nombre de usuario a buscar
     * @return Un Optional que contiene el usuario si existe
     */
    @Override
    public Optional<Usuario> findByUsername(String username) {
        String sql = "SELECT * FROM usuarios WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("rol"),
                            rs.getString("nombre"),
                            rs.getString("email")
                    );
                    return Optional.of(usuario);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por username: " + e.getMessage());
            e.printStackTrace();
        }
        
        return Optional.empty();
    }
    
    /**
     * Verifica las credenciales de un usuario para el inicio de sesión.
     * @param username Nombre de usuario
     * @param password Contraseña sin encriptar
     * @return Un Optional que contiene el usuario si las credenciales son válidas
     */
    @Override
    public Optional<Usuario> authenticate(String username, String password) {
        Optional<Usuario> optionalUsuario = findByUsername(username);
        
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            // Verificar la contraseña usando BCrypt
            if (BCrypt.checkpw(password, usuario.getPassword())) {
                return optionalUsuario;
            }
        }
        
        return Optional.empty();
    }
}