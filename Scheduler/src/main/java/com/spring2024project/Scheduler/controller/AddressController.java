package com.spring2024project.Scheduler.controller;

import com.spring2024project.Scheduler.entity.Address;
import com.spring2024project.Scheduler.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addresses")
public class AddressController implements BaseController<Address,Integer>{

    private final BaseService<Address> addressService;

    @Autowired
    public AddressController(BaseService<Address> addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public ResponseEntity<List<Address>> getAll() {
        List<Address> addresses = addressService.getAll();
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Address> getById(@PathVariable Integer id) {
        Address address = addressService.getById(id);
        return address.getBuildingNumber() == 0 ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(address, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Address> create(@RequestBody Address address) {
        Address createdAddress = addressService.create(address);
        return new ResponseEntity<>(createdAddress, HttpStatus.CREATED);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Address> update(@PathVariable Integer id, @RequestBody Address address) {
        Address updatedAddress = addressService.update(id,address);
        return updatedAddress.getBuildingNumber() == 0 ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Address> delete(@PathVariable Integer id) {
        var deletedAddress = addressService.delete(id);
        return deletedAddress.getId() == 0 ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(deletedAddress, HttpStatus.OK);
    }

}