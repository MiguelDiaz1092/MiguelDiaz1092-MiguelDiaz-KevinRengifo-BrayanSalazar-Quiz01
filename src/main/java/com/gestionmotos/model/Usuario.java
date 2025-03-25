package com.gestionmotos.model;

/**
 * Clase que representa un usuario del sistema.
 */
public class Usuario {
    private int id;
    private String username;
    private String password;
    private String rol;
    private String nombre;
    private String email;
    
    // Constructor vacío
    public Usuario() {
    }
    
    // Constructor con todos los campos excepto id
    public Usuario(String username, String password, String rol, String nombre, String email) {
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.nombre = nombre;
        this.email = email;
    }
    
    // Constructor con todos los campos
    public Usuario(int id, String username, String password, String rol, String nombre, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.nombre = nombre;
        this.email = email;
    }
    
    // Getters y setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRol() {
        return rol;
    }
    
    public void setRol(String rol) {
        this.rol = rol;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    @Override
    public String toString() {
        return "Usuario{" +
               "id=" + id +
               ", username='" + username + '\'' +
               ", rol='" + rol + '\'' +
               ", nombre='" + nombre + '\'' +
               ", email='" + email + '\'' +
               '}';
    }
}