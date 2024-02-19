package com.spring2024project.Scheduler.service;

import com.spring2024project.Scheduler.entity.Address;
import com.spring2024project.Scheduler.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.spring2024project.Scheduler.entity.Address.*;

import java.util.List;

@Service
public class AddressServiceImp implements AddressService {
    private AddressRepository ar;

    @Autowired
    public AddressServiceImp(AddressRepository ar) {
        this.ar = ar;
    }

    @Override
    public List<Address> getAllAddresses() {
        return (List<Address>) ar.findAll();
    }

    @Override
    public Address getAddressById(int id) {
        return ar.findById(id).orElse(defaultAddress());
    }

    @Override
    public Address createAddress(int buildingNumber, String street, String city, String state, int zipcode) {
        Address address = of(buildingNumber, street, city, state, zipcode);
        return ar.save(address);
    }

    @Override
    public Address updateAddress(int id, int buildingNumber, String street, String city, String state, int zipcode) {
        Address toUpdate = getAddressById(id);
        if (toUpdate.getId() != 0) {
            toUpdate.setBuildingNumber(buildingNumber);
            toUpdate.setStreet(street);
            toUpdate.setCity(city);
            toUpdate.setState(state);
            toUpdate.setZipcode(zipcode);
            return ar.save(toUpdate);
        }
        return toUpdate;
    }

    @Override
    public Address deleteAddress(int id) {
        var toDelete = getAddressById(id);
        if (toDelete.getId() != 0) {
            var deleted = from(toDelete);
            ar.deleteById(id);
            return deleted;
        }
        return toDelete;
    }
}
