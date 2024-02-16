package com.spring2024project.Scheduler.controller;

import com.spring2024project.Scheduler.entity.Generator;
import com.spring2024project.Scheduler.service.GeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/generators")
public class GeneratorController {

    private final GeneratorService generatorService;

    @Autowired
    public GeneratorController(GeneratorService generatorService) {
        this.generatorService = generatorService;
    }

    @GetMapping
    public ResponseEntity<List<Generator>> getAllGenerators() {
        List<Generator> generators = generatorService.getAllGenerators();
        return new ResponseEntity<>(generators, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Generator> getGeneratorById(@PathVariable int id) {
        Generator generator = generatorService.getGeneratorById(id);
        return generator.getManufacturer().isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(generator, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Generator> createGenerator(@RequestBody Generator generator) {
        Generator createdGenerator = generatorService.createGenerator(generator.getManufacturer(), generator.getkWSize(), generator.getInstallInstructions());
        return new ResponseEntity<>(createdGenerator, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Generator> updateGenerator(@PathVariable int id, @RequestBody Generator generator) {
        Generator updatedGenerator = generatorService.updateGenerator(id, generator.getManufacturer(), generator.getkWSize(), generator.getInstallInstructions());
        return updatedGenerator.getManufacturer().isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(updatedGenerator, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Generator> deleteGenerator(@PathVariable int id) {
        Generator deletedGenerator = generatorService.deleteGenerator(id);
        return deletedGenerator.getManufacturer().isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(deletedGenerator, HttpStatus.OK);
    }
}
