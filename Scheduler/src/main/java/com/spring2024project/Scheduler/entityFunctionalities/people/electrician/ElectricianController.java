package com.spring2024project.Scheduler.entityFunctionalities.people.electrician;

import com.spring2024project.Scheduler.entityFunctionalities.address.AddressDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/electricians")
public class ElectricianController  {

    private final ElectricianService es;

    @Autowired
    public ElectricianController(ElectricianService es) {
        this.es = es;
    }

    @GetMapping
    public ResponseEntity<List<Electrician>> getAll() {
        var electricians = es.getAll();
        return new ResponseEntity<>(electricians, HttpStatus.FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Electrician> getById(@PathVariable int id) {
        Electrician electrician = es.getById(id);
        return electrician.getId() == 0 ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(electrician, HttpStatus.FOUND);

    }

    @GetMapping("/search")
    public ResponseEntity<List<Electrician>> getByFirstAndLastName(@RequestParam String firstName, @RequestParam String lastName) {
        List<Electrician> electricians = es.findByFirstAndLastName(firstName, lastName);
        return electricians.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(electricians, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Electrician> create(@RequestBody Electrician entity) {
        Electrician created = es.create(entity);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/personalDetails/{id}")
    public ResponseEntity<Electrician> updatePersonalDetails(@PathVariable int id, @RequestBody ElectricianPersonalDetailsDto personalDetailsDto) {
        Electrician updated = es.updatePersonalDetails(id, personalDetailsDto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PutMapping("/homeAddress/{id}")
    public ResponseEntity<Electrician> updateHomeAddress(@PathVariable int id, @RequestBody AddressDto addressDto) {
        Electrician updated = es.updateHomeAddress(id, addressDto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ElectricianPersonalDetailsDto> delete(@PathVariable int id) {
        var deletedElectrician = es.delete(id);
        return deletedElectrician.firstName().equals("") ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(deletedElectrician, HttpStatus.OK);
    }
}
