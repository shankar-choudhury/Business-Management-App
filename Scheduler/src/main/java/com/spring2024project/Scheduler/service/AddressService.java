package com.spring2024project.Scheduler.service;

import com.spring2024project.Scheduler.entity.Address;
import com.spring2024project.Scheduler.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.spring2024project.Scheduler.entity.Address.*;

import java.util.List;

/**
 * This service class provides functionality to perform CRUD operations on Address entities.
 * It implements the BaseService interface.
 * @Author Shankar Choudhury
 */
@Service
public class AddressService implements BaseService<Address> {
    private AddressRepository ar;

    /**
     * Constructs an AddressService instance with the given AddressRepository.
     * @param ar The AddressRepository to be used by the service.
     */
    @Autowired
    public AddressService(AddressRepository ar) {
        this.ar = ar;
    }

    /**
     * Retrieves all addresses from the database.
     * @return A list of all addresses.
     */
    @Override
    public List<Address> getAll() {
        return (List<Address>) ar.findAll();
    }

    /**
     * Retrieves an address by its ID from the database.
     * @param id The ID of the address to retrieve.
     * @return The address with the specified ID, or a default address if not found.
     */
    @Override
    public Address getById(int id) {
        return ar.findById(id).orElse(Address.defaultAddress());
    }

    /**
     * Creates a new address in the database.
     * @param entity The address entity to create.
     * @return The created address.
     */
    @Override
    public Address create(Address entity) {
        Address address = Address.from(entity);
        return ar.save(address);
    }

    /**
     * Updates an existing address in the database.
     * @param id The ID of the address to update.
     * @param entity The updated address entity.
     * @return The updated address.
     */
    @Override
    public Address update(int id, Address entity) {
        Address toUpdate = getById(id);
        if (toUpdate.getId() != 0) {
            toUpdate = Address.from(entity);
            return ar.save(toUpdate);
        }
        return toUpdate;
    }

    /**
     * Deletes an address from the database.
     * @param id The ID of the address to delete.
     * @return The deleted address, or a default address if not found.
     */
    @Override
    public Address delete(int id) {
        var toDelete = getById(id);
        if (toDelete.getId() != 0) {
            var deleted = Address.fromDeleted(toDelete);
            ar.deleteById(id);
            return deleted;
        }
        return toDelete;
    }
}
