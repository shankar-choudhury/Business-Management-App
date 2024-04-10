package com.spring2024project.Scheduler.entityFunctionalities.address;

import com.spring2024project.Scheduler.entityFunctionalities.BaseController;
import com.spring2024project.Scheduler.entityFunctionalities.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing Address entities.
 * @Author Shankar Choudhury
 */
@RestController
@RequestMapping("/addresses")
public class AddressController implements BaseController<Address> {

    private final BaseService<Address> addressService;

    /**
     * Constructor for AddressController.
     * @param addressService The service responsible for handling Address-related operations.
     */
    @Autowired
    public AddressController(BaseService<Address> addressService) {
        this.addressService = addressService;
    }

    /**
     * Retrieve all addresses.
     * @return ResponseEntity containing a list of Address objects.
     */
    @Override
    @GetMapping
    public ResponseEntity<List<Address>> getAll() {
        List<Address> addresses = addressService.getAll();
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    /**
     * Retrieve an address by its ID.
     * @param id The ID of the address to retrieve.
     * @return ResponseEntity containing the retrieved Address object.
     */
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Address> getById(@PathVariable int id) {
        Address address = addressService.getById(id);
        return address.getBuildingNumber().isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(address, HttpStatus.OK);
    }

    /**
     * Create a new address.
     * @param address The Address object to create.
     * @return ResponseEntity containing the created Address object.
     */
    @Override
    @PostMapping(consumes = {"application/json"})
    public ResponseEntity<Address> create(@RequestBody Address address) {
        Address createdAddress = addressService.create(address);
        return new ResponseEntity<>(createdAddress, HttpStatus.CREATED);
    }

    /**
     * Update an existing address.
     * @param id The ID of the address to update.
     * @param address The updated Address object.
     * @return ResponseEntity containing the updated Address object.
     */
    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Address> update(@PathVariable int id, @RequestBody Address address) {
        Address updatedAddress = addressService.update(id,address);
        return updatedAddress.getBuildingNumber().isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }

    /**
     * Delete an address by its ID.
     * @param id The ID of the address to delete.
     * @return ResponseEntity containing the deleted Address object.
     */
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Address> delete(@PathVariable int id) {
        var deletedAddress = addressService.delete(id);
        return deletedAddress.getId() == 0 ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(deletedAddress, HttpStatus.OK);
    }

}