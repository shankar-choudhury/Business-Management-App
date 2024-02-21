package com.spring2024project.Scheduler.service;

import java.util.List;

public interface BaseService<T> {
    List<T> getAll();
    T getById(int id);
    T create(T entity);
    T update(int id, T entity);
    T delete(int id);
}
