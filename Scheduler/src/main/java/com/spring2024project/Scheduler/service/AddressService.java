package com.spring2024project.Scheduler.service;

import com.spring2024project.Scheduler.entity.Address;
import com.spring2024project.Scheduler.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.spring2024project.Scheduler.entity.Address.*;

import java.util.List;

@Service
public class AddressService implements BaseService<Address> {
    private AddressRepository ar;

    @Autowired
    public AddressService(AddressRepository ar) {
        this.ar = ar;
    }

    @Override
    public List<Address> getAll() {
        return (List<Address>) ar.findAll();
    }

    @Override
    public Address getById(int id) {
        return ar.findById(id).orElse(defaultAddress());
    }

    @Override
    public Address create(Address entity) {
        Address address = from(entity);
        return ar.save(address);
    }

    @Override
    public Address update(int id, Address entity) {
        Address toUpdate = getById(id);
        if (toUpdate.getId() != 0) {
            toUpdate.setBuildingNumber(entity.getBuildingNumber());
            toUpdate.setStreet(entity.getStreet());
            toUpdate.setCity(entity.getCity());
            toUpdate.setState(entity.getState());
            toUpdate.setZipcode(entity.getZipcode());
            return ar.save(toUpdate);
        }
        return toUpdate;
    }

    @Override
    public Address delete(int id) {
        var toDelete = getById(id);
        if (toDelete.getId() != 0) {
            var deleted = fromDeleted(toDelete);
            ar.deleteById(id);
            return deleted;
        }
        return toDelete;
    }

}
