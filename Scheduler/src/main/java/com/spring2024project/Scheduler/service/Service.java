package com.spring2024project.Scheduler.service;

import java.util.List;

public interface Service<T> {
    List<T> getAll();
    T getById(int id);
    T create(Object... objects);
    T update(int id, Object... objects);
    T delete(int id);
}
