package com.spring2024project.Scheduler.entityFunctionalities.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {
    private final JobService js;
    @Autowired
    public JobController(JobService js) {
        this.js = js;
    }

    @GetMapping
    public ResponseEntity<List<Job>> getAll() {
        var jobs = js.getAll();
        return new ResponseEntity<>(jobs, HttpStatus.FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getById(@PathVariable int id) {
        var searchedJob = js.getById(id);
        return searchedJob.getId() != 0 ?
                new ResponseEntity<>(searchedJob, HttpStatus.FOUND) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Job> create(@RequestBody JobCreationDto jobCreationDto) {
        var createdJob = js.create(jobCreationDto);
        return new ResponseEntity<>(createdJob, HttpStatus.CREATED);
    }


}
