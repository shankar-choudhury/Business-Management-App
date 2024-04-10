package com.spring2024project.Scheduler.entityFunctionalities;

import java.util.List;

/**
 * This interface represents a set of common CRUD operations that are expected by the various entities of the application.
 * Any additional services that are added onto the application are expected to implement the BaseService interface, as it
 * streamlines and clarifies the development purpose. If future operations are becoming common amongst the services, the
 * developer should consider adding them as abstract methods to this interface.
 * @param <T> The type of the entity the service will be performing operations with
 * @Author Shankar Choudhury
 */
public interface BaseService<T> {
    List<T> getAll();
    T getById(int id);
    T create(T entity);
    T update(int id, T entity);
    T delete(int id);
}
