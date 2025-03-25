package com.gestionmotos.repository;

import com.gestionmotos.model.Motocicleta;
import java.util.List;

/**
 * Interfaz específica para el repositorio de motocicletas.
 * Extiende la interfaz genérica Repository y añade métodos específicos.
 */
public interface MotocicletaRepository extends Repository<Motocicleta, Integer> {
    
    /**
     * Busca motocicletas por marca.
     * @param marca La marca a buscar
     * @return Lista de motocicletas que coinciden con la marca
     */
    List<Motocicleta> findByMarca(String marca);
    
    /**
     * Busca motocicletas con precio menor o igual al especificado.
     * @param precio El precio máximo
     * @return Lista de motocicletas con precio menor o igual
     */
    List<Motocicleta> findByPrecioMaximo(double precio);
    
    /**
     * Busca motocicletas con cilindraje dentro de un rango.
     * @param min Cilindraje mínimo
     * @param max Cilindraje máximo
     * @return Lista de motocicletas dentro del rango de cilindraje
     */
    List<Motocicleta> findByCilindrajeRango(int min, int max);
}