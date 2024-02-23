package com.spring2024project.Scheduler.controller;

import com.spring2024project.Scheduler.entity.Generator;
import com.spring2024project.Scheduler.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for handling HTTP requests related to generators.
 * @Author Shankar Choudhury
 */
@RestController
@RequestMapping("/generators")
public class GeneratorController implements BaseController<Generator> {

    private final BaseService<Generator> generatorService;

    /**
     * Constructs a new GeneratorController instance.
     * @param generatorService The service responsible for handling generator-related operations.
     */
    @Autowired
    public GeneratorController(BaseService<Generator> generatorService) {
        this.generatorService = generatorService;
    }

    /**
     * Retrieves all generators.
     * @return A ResponseEntity containing a list of generators and an HTTP status code.
     */
    @GetMapping
    public ResponseEntity<List<Generator>> getAll() {
        List<Generator> generators = generatorService.getAll();
        return new ResponseEntity<>(generators, HttpStatus.OK);
    }

    /**
     * Retrieves a generator by its ID.
     * @param id The ID of the generator to retrieve.
     * @return A ResponseEntity containing the requested generator and an HTTP status code.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Generator> getById(@PathVariable int id) {
        Generator generator = generatorService.getById(id);
        return generator.getManufacturer().isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(generator, HttpStatus.OK);
    }

    /**
     * Creates a new generator.
     * @param generator The generator to create.
     * @return A ResponseEntity containing the created generator and an HTTP status code.
     */
    @PostMapping
    public ResponseEntity<Generator> create(@RequestBody Generator generator) {
        Generator createdGenerator = generatorService.create(generator);
        return new ResponseEntity<>(createdGenerator, HttpStatus.CREATED);
    }

    /**
     * Updates an existing generator.
     * @param id The ID of the generator to update.
     * @param generator The updated generator data.
     * @return A ResponseEntity containing the updated generator and an HTTP status code.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Generator> update(@PathVariable int id, @RequestBody Generator generator) {
        Generator updatedGenerator = generatorService.update(id, generator);
        return updatedGenerator.getManufacturer().isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(updatedGenerator, HttpStatus.OK);
    }

    /**
     * Deletes a generator by its ID.
     * @param id The ID of the generator to delete.
     * @return A ResponseEntity containing the deleted generator and an HTTP status code.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Generator> delete(@PathVariable int id) {
        Generator deletedGenerator = generatorService.delete(id);
        return deletedGenerator.getManufacturer().isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(deletedGenerator, HttpStatus.OK);
    }
}
