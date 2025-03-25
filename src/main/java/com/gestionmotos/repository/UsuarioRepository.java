package com.gestionmotos.repository;

import com.gestionmotos.model.Usuario;
import java.util.Optional;

/**
 * Interfaz específica para el repositorio de usuarios.
 * Extiende la interfaz genérica Repository y añade métodos específicos.
 */
public interface UsuarioRepository extends Repository<Usuario, Integer> {
    
    /**
     * Busca un usuario por su nombre de usuario.
     * @param username El nombre de usuario a buscar
     * @return Un Optional que contiene el usuario si existe
     */
    Optional<Usuario> findByUsername(String username);
    
    /**
     * Verifica las credenciales de un usuario para el inicio de sesión.
     * @param username Nombre de usuario
     * @param password Contraseña sin encriptar
     * @return Un Optional que contiene el usuario si las credenciales son válidas
     */
    Optional<Usuario> authenticate(String username, String password);
}