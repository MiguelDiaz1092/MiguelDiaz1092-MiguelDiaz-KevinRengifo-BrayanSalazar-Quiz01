package com.gestionmotos.repository;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz genérica para implementar el patrón Repository.
 * @param <T> Tipo de entidad
 * @param <ID> Tipo del identificador de la entidad
 */
public interface Repository<T, ID> {
    
    /**
     * Guarda una entidad en el repositorio.
     * @param entity La entidad a guardar
     * @return La entidad guardada con su ID asignado
     */
    T save(T entity);
    
    /**
     * Actualiza una entidad existente.
     * @param entity La entidad a actualizar
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    boolean update(T entity);
    
    /**
     * Elimina una entidad por su ID.
     * @param id El ID de la entidad a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     */
    boolean deleteById(ID id);
    
    /**
     * Busca una entidad por su ID.
     * @param id El ID de la entidad a buscar
     * @return Un Optional que contiene la entidad si existe
     */
    Optional<T> findById(ID id);
    
    /**
     * Recupera todas las entidades del repositorio.
     * @return Una lista con todas las entidades
     */
    List<T> findAll();
}