package com.spring2024project.Scheduler.controller;

import com.spring2024project.Scheduler.entity.Address;
import com.spring2024project.Scheduler.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public ResponseEntity<List<Address>> getAllAddresses() {
        List<Address> addresses = addressService.getAllAddresses();
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddressById(@PathVariable int id) {
        Address address = addressService.getAddressById(id);
        return address.getBuildingNumber() == 0 ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(address, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Address> createAddress(@RequestBody Address address) {
        Address createdAddress =
                addressService
                        .createAddress(
                                address.getBuildingNumber(),
                                address.getStreet(),
                                address.getCity(),
                                address.getState(),
                                address.getZipcode());
        return new ResponseEntity<>(createdAddress, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Address> updateAddress(@PathVariable int id, @RequestBody Address address) {
        Address updatedAddress = addressService.updateAddress(id, address.getBuildingNumber(), address.getStreet(), address.getCity(), address.getState(), address.getZipcode());
        return updatedAddress.getBuildingNumber() == 0 ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Address> deleteAddress(@PathVariable int id) {
        var deletedAddress = addressService.deleteAddress(id);
        return deletedAddress.getId() == 0 ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(deletedAddress, HttpStatus.OK);
    }

}