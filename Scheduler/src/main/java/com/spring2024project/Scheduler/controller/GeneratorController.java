package com.spring2024project.Scheduler.controller;

import com.spring2024project.Scheduler.entity.Generator;
import com.spring2024project.Scheduler.service.BaseService;
import com.spring2024project.Scheduler.service.GeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/generators")
public class GeneratorController {

    private final BaseService<Generator> generatorService;

    @Autowired
    public GeneratorController(BaseService<Generator> generatorService) {
        this.generatorService = generatorService;
    }

    @GetMapping
    public ResponseEntity<List<Generator>> getAllGenerators() {
        List<Generator> generators = generatorService.getAll();
        return new ResponseEntity<>(generators, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Generator> getGeneratorById(@PathVariable int id) {
        Generator generator = generatorService.getById(id);
        return generator.getManufacturer().isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(generator, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Generator> createGenerator(@RequestBody Generator generator) {
        Generator createdGenerator = generatorService.create(generator);
        return new ResponseEntity<>(createdGenerator, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Generator> updateGenerator(@PathVariable int id, @RequestBody Generator generator) {
        Generator updatedGenerator = generatorService.update(id, generator);
        return updatedGenerator.getManufacturer().isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(updatedGenerator, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Generator> deleteGenerator(@PathVariable int id) {
        Generator deletedGenerator = generatorService.delete(id);
        return deletedGenerator.getManufacturer().isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(deletedGenerator, HttpStatus.OK);
    }
}
