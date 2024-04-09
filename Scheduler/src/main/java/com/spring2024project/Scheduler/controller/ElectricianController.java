package com.spring2024project.Scheduler.controller;

import com.spring2024project.Scheduler.entity.employees.Electrician;
import com.spring2024project.Scheduler.service.ElectricianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/electricians")
public class ElectricianController implements BaseController<Electrician> {

    private final ElectricianService es;

    @Autowired
    public ElectricianController(ElectricianService es) {
        this.es = es;
    }
    @Override
    @GetMapping
    public ResponseEntity<List<Electrician>> getAll() {
        var electricians = es.getAll();
        return new ResponseEntity<>(electricians, HttpStatus.FOUND);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Electrician> getById(@PathVariable int id) {
        Electrician electrician = es.getById(id);
        return electrician.getId() == 0 ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(electrician, HttpStatus.FOUND);

    }

    @Override
    @PostMapping
    public ResponseEntity<Electrician> create(@RequestBody Electrician entity) {
        Electrician electrician = es.create(entity);
        return new ResponseEntity<>(electrician, HttpStatus.CREATED);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Electrician> update(@PathVariable int id, @RequestBody Electrician entity) {
        Electrician updatedElectrician = es.update(id, entity);
        return updatedElectrician.getId() == 0 ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(updatedElectrician, HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Electrician> delete(@PathVariable int id) {
        Electrician deletedElectrician = es.delete(id);
        return deletedElectrician.getId() == 0 ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(deletedElectrician, HttpStatus.OK);
    }
}
