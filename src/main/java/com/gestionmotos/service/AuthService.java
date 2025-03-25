package com.gestionmotos.service;

import com.gestionmotos.model.Usuario;
import com.gestionmotos.repository.UsuarioRepository;
import com.gestionmotos.repository.UsuarioRepositoryImpl;

import java.util.Optional;

/**
 * Servicio para gestionar la autenticación de usuarios.
 */
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private Usuario currentUser;
    
    // Singleton
    private static AuthService instance;
    
    /**
     * Constructor privado para Singleton.
     */
    private AuthService() {
        this.usuarioRepository = new UsuarioRepositoryImpl();
    }
    
    /**
     * Obtiene la instancia única del servicio.
     * @return Instancia del AuthService
     */
    public static synchronized AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }
    
    /**
     * Intenta autenticar a un usuario con sus credenciales.
     * @param username Nombre de usuario
     * @param password Contraseña
     * @return true si la autenticación fue exitosa, false en caso contrario
     */
    public boolean login(String username, String password) {
        Optional<Usuario> optionalUsuario = usuarioRepository.authenticate(username, password);
        
        if (optionalUsuario.isPresent()) {
            this.currentUser = optionalUsuario.get();
            return true;
        }
        
        return false;
    }
    
    /**
     * Cierra la sesión del usuario actual.
     */
    public void logout() {
        this.currentUser = null;
    }
    
    /**
     * Verifica si hay un usuario con sesión iniciada.
     * @return true si hay un usuario autenticado, false en caso contrario
     */
    public boolean isAuthenticated() {
        return currentUser != null;
    }
    
    /**
     * Verifica si el usuario actual es administrador.
     * @return true si el usuario es administrador, false en caso contrario
     */
    public boolean isAdmin() {
        return isAuthenticated() && "admin".equals(currentUser.getRol());
    }
    
    /**
     * Obtiene el usuario autenticado actualmente.
     * @return El usuario actual o null si no hay sesión iniciada
     */
    public Usuario getCurrentUser() {
        return currentUser;
    }
}