package com.gestionmotos.model;

/**
 * Clase que representa una motocicleta en el sistema.
 */
public class Motocicleta {
    private int id;
    private String marca;
    private int cilindraje;
    private double precio;
    private String color;
    
    // Constructor vacío
    public Motocicleta() {
    }
    
    // Constructor con todos los campos excepto id
    public Motocicleta(String marca, int cilindraje, double precio, String color) {
        this.marca = marca;
        this.cilindraje = cilindraje;
        this.precio = precio;
        this.color = color;
    }
    
    // Constructor con todos los campos
    public Motocicleta(int id, String marca, int cilindraje, double precio, String color) {
        this.id = id;
        this.marca = marca;
        this.cilindraje = cilindraje;
        this.precio = precio;
        this.color = color;
    }
    
    // Getters y setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getMarca() {
        return marca;
    }
    
    public void setMarca(String marca) {
        this.marca = marca;
    }
    
    public int getCilindraje() {
        return cilindraje;
    }
    
    public void setCilindraje(int cilindraje) {
        this.cilindraje = cilindraje;
    }
    
    public double getPrecio() {
        return precio;
    }
    
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    @Override
    public String toString() {
        return "Motocicleta{" +
               "id=" + id +
               ", marca='" + marca + '\'' +
               ", cilindraje=" + cilindraje +
               ", precio=" + precio +
               ", color='" + color + '\'' +
               '}';
    }
}