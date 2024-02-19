package com.spring2024project.Scheduler.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface BaseController<T, ID> {
    ResponseEntity<List<T>> getAll();
    ResponseEntity<T> getById(@PathVariable ID id);
    ResponseEntity<T> create(@RequestBody T entity);
    ResponseEntity<T> update(@PathVariable ID id, @RequestBody T entity);
    ResponseEntity<T> delete(@PathVariable ID id);
}
