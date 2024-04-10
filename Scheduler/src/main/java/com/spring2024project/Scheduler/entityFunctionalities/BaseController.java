package com.spring2024project.Scheduler.entityFunctionalities;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * This interface represents common Controller operations that are required for baseline activities. Future controllers
 * should implement BaseController. Developers should consider adding abstract method to BaseController if other
 * controllers share similar methods
 * @param <T> Type of entity controller uses for persistence.
 */
public interface BaseController<T> {

    /**
     * TODO: Implement CRUD operations for T controller, class will have a private T service to call operations on in CRUD methods
     */

    /**
     * Return a list of all entities of this type
     * @return A list of all entities of this type
     */
    ResponseEntity<List<T>> getAll();

    /**
     * Return a specific instance of this entity determined by its key
     * @param id Primary key to identify specific entity by
     * @return A specific instance of this entity determined by its key, or an empty instance of this entity if not found
     */
    ResponseEntity<T> getById(@PathVariable int id);

    /**
     * Create and store a specific instance of this entity
     * @param entity Entity to build extract information from and feed to service
     * @return Created instance of entity
     */
    ResponseEntity<T> create(@RequestBody T entity);

    /**
     * Find and update a specific instance of this entity
     * @param id Primary key for this entity
     * @param entity Entity instance to extract information and update corresponding Entity with
     * @return Updated instance of entity, or an empty entity if id does not map to an existing entity
     */
    ResponseEntity<T> update(@PathVariable int id, @RequestBody T entity);

    /**
     * Find and delete a specific instance of this entity
     * @param id Primary key for this entity
     * @return Deleted entity, or an empty entity if id does not map to an existing entity
     */
    ResponseEntity<T> delete(@PathVariable int id);
}
