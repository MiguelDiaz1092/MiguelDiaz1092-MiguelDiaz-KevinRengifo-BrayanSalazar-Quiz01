package com.gestionmotos.repository;

import com.gestionmotos.model.Motocicleta;
import com.gestionmotos.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del repositorio de motocicletas que accede a la base de datos.
 */
public class MotocicletaRepositoryImpl implements MotocicletaRepository {
    
    /**
     * Guarda una nueva motocicleta en la base de datos.
     * @param moto La motocicleta a guardar
     * @return La motocicleta guardada con su ID asignado
     */
    @Override
    public Motocicleta save(Motocicleta moto) {
        String sql = "INSERT INTO motocicletas (marca, cilindraje, precio, color) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, moto.getMarca());
            stmt.setInt(2, moto.getCilindraje());
            stmt.setDouble(3, moto.getPrecio());
            stmt.setString(4, moto.getColor());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Crear motocicleta falló, ninguna fila afectada.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    moto.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Crear motocicleta falló, ningún ID obtenido.");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al guardar motocicleta: " + e.getMessage());
            e.printStackTrace();
        }
        
        return moto;
    }
    
    /**
     * Actualiza una motocicleta existente en la base de datos.
     * @param moto La motocicleta a actualizar
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    @Override
    public boolean update(Motocicleta moto) {
        String sql = "UPDATE motocicletas SET marca = ?, cilindraje = ?, precio = ?, color = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, moto.getMarca());
            stmt.setInt(2, moto.getCilindraje());
            stmt.setDouble(3, moto.getPrecio());
            stmt.setString(4, moto.getColor());
            stmt.setInt(5, moto.getId());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar motocicleta: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Elimina una motocicleta por su ID.
     * @param id El ID de la motocicleta a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     */
    @Override
    public boolean deleteById(Integer id) {
        String sql = "DELETE FROM motocicletas WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar motocicleta: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Busca una motocicleta por su ID.
     * @param id El ID de la motocicleta a buscar
     * @return Un Optional que contiene la motocicleta si existe
     */
    @Override
    public Optional<Motocicleta> findById(Integer id) {
        String sql = "SELECT * FROM motocicletas WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Motocicleta moto = new Motocicleta(
                            rs.getInt("id"),
                            rs.getString("marca"),
                            rs.getInt("cilindraje"),
                            rs.getDouble("precio"),
                            rs.getString("color")
                    );
                    return Optional.of(moto);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar motocicleta por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return Optional.empty();
    }
    
    /**
     * Recupera todas las motocicletas de la base de datos.
     * @return Una lista con todas las motocicletas
     */
    @Override
    public List<Motocicleta> findAll() {
        String sql = "SELECT * FROM motocicletas";
        List<Motocicleta> motos = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Motocicleta moto = new Motocicleta(
                        rs.getInt("id"),
                        rs.getString("marca"),
                        rs.getInt("cilindraje"),
                        rs.getDouble("precio"),
                        rs.getString("color")
                );
                motos.add(moto);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las motocicletas: " + e.getMessage());
            e.printStackTrace();
        }
        
        return motos;
    }
    
    /**
     * Busca motocicletas por marca.
     * @param marca La marca a buscar
     * @return Lista de motocicletas que coinciden con la marca
     */
    @Override
    public List<Motocicleta> findByMarca(String marca) {
        String sql = "SELECT * FROM motocicletas WHERE marca LIKE ?";
        List<Motocicleta> motos = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + marca + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Motocicleta moto = new Motocicleta(
                            rs.getInt("id"),
                            rs.getString("marca"),
                            rs.getInt("cilindraje"),
                            rs.getDouble("precio"),
                            rs.getString("color")
                    );
                    motos.add(moto);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar motocicletas por marca: " + e.getMessage());
            e.printStackTrace();
        }
        
        return motos;
    }
    
    /**
     * Busca motocicletas con precio menor o igual al especificado.
     * @param precio El precio máximo
     * @return Lista de motocicletas con precio menor o igual
     */
    @Override
    public List<Motocicleta> findByPrecioMaximo(double precio) {
        String sql = "SELECT * FROM motocicletas WHERE precio <= ?";
        List<Motocicleta> motos = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, precio);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Motocicleta moto = new Motocicleta(
                            rs.getInt("id"),
                            rs.getString("marca"),
                            rs.getInt("cilindraje"),
                            rs.getDouble("precio"),
                            rs.getString("color")
                    );
                    motos.add(moto);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar motocicletas por precio máximo: " + e.getMessage());
            e.printStackTrace();
        }
        
        return motos;
    }
    
    /**
     * Busca motocicletas con cilindraje dentro de un rango.
     * @param min Cilindraje mínimo
     * @param max Cilindraje máximo
     * @return Lista de motocicletas dentro del rango de cilindraje
     */
    @Override
    public List<Motocicleta> findByCilindrajeRango(int min, int max) {
        String sql = "SELECT * FROM motocicletas WHERE cilindraje BETWEEN ? AND ?";
        List<Motocicleta> motos = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, min);
            stmt.setInt(2, max);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Motocicleta moto = new Motocicleta(
                            rs.getInt("id"),
                            rs.getString("marca"),
                            rs.getInt("cilindraje"),
                            rs.getDouble("precio"),
                            rs.getString("color")
                    );
                    motos.add(moto);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar motocicletas por rango de cilindraje: " + e.getMessage());
            e.printStackTrace();
        }
        
        return motos;
    }
}