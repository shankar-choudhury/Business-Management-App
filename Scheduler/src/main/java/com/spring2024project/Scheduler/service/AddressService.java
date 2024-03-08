package com.spring2024project.Scheduler.service;

import com.spring2024project.Scheduler.entity.Address;

import com.spring2024project.Scheduler.entity.CreditCard;
import com.spring2024project.Scheduler.entity.Customer;
import com.spring2024project.Scheduler.repository.AddressRepository;
import com.spring2024project.Scheduler.customValidatorTags.ZipCodeValidatorTag;
import com.spring2024project.Scheduler.repository.CreditCardRepository;
import com.spring2024project.Scheduler.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This service class provides functionality to perform CRUD operations on Address entities.
 * It implements the BaseService interface.
 * @Author Shankar Choudhury
 */
@Service
public class AddressService implements BaseService<Address> {
    private final AddressRepository ar;
    private final CustomerRepository cr;
    private final CreditCardRepository ccr;
    private ZipCodeValidatorTag v;

    /**
     * Constructs an AddressService instance with the given repositories and validator.
     * @param addressRepository The AddressRepository to be used by the service.
     * @param zipCodeValidatorTag The ZipCodeValidatorTag to be used by the service.
     * @param customerRepository The CustomerRepository to be used by the service.
     * @param creditCardRepository The CreditCardRepository to be used by the service.
     */
    @Autowired
    public AddressService(AddressRepository addressRepository,
                          CustomerRepository customerRepository,
                          CreditCardRepository creditCardRepository,
                          ZipCodeValidatorTag zipCodeValidatorTag) {
        this.ar = addressRepository;
        this.cr = customerRepository;
        this.ccr = creditCardRepository;
        this.v = zipCodeValidatorTag;
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
        return ar.findById(id).orElse(Address.emptyAddress());
    }

    /**
     * Creates a new address in the database.
     * @param entity The address entity to create.
     * @return The created address.
     */
    @Override
    public Address create(Address entity) {
        Address address = Address.from(entity, v);
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
        Address original = getById(id);
        if (original.getId() != 0) {
            var updated = Address.from(entity, v);
            var oldCopy = delete(id);
            var newCopy = ar.save(updated);
            updateAssociatedEntities(oldCopy, newCopy);
        }
        return original;
    }

    private void updateAssociatedEntities(Address originalAddress, Address updatedAddress) {
        Customer customer = originalAddress.getCustomer();
        if (customer != null) {
            // Update the address in the associated customer entity
            customer.getAddressList().remove(originalAddress);
            customer.getAddressList().add(updatedAddress);
            cr.save(customer);
        }

        List<CreditCard> creditCards = originalAddress.getCreditCardList();
        if (creditCards != null) {
            // Update the address in the associated credit card entities
            for (CreditCard creditCard : creditCards) {
                creditCard.setBillingAddress(updatedAddress);
                ccr.save(creditCard);
            }
        }
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
