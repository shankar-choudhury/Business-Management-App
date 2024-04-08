package com.spring2024project.Scheduler.service;

import com.spring2024project.Scheduler.customValidatorTags.ZipCodeValidatorTag;
import com.spring2024project.Scheduler.entity.Address;
import com.spring2024project.Scheduler.entity.Customer;
import com.spring2024project.Scheduler.entity.employees.Electrician;
import com.spring2024project.Scheduler.repository.ElectricianRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ElectricianService implements BaseService<Electrician> {

    private final ElectricianRepository er;
    private final AddressService as;
    private EntityManager em;

    @Autowired
    public ElectricianService(ElectricianRepository er, AddressService as, EntityManager em) {
        this.er = er;
        this.as = as;
        this.em = em;
    }
    @Override
    public List<Electrician> getAll() {
        return (List<Electrician>) er.findAll();
    }

    @Override
    public Electrician getById(int id) {
        return er.findById(id).orElse(Electrician.emptyElectrician());
    }

    @Override
    public Electrician create(Electrician entity) {

        try {
            var newElectrician = Electrician.from(entity);

            // Check for existing addresses and associate them with the customer
            List<Address> existingAddresses = as.getAll();
            var homeAddress = entity.getHomeAddress();
            as.validateAndNormalizeAddress(homeAddress);
            newElectrician.setHomeAddress(homeAddress);

            // Merge the customer entity
            em.merge(newElectrician);

            return newElectrician;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create electrician", e);
        }
    }

    @Override
    public Electrician update(int id, Electrician entity) {
        Electrician original = getById(id);
        if (original.getId() != 0)
            return er.save(Electrician.from(entity));
        else
            return original;
    }

    @Override
    public Electrician delete(int id) {
        Electrician toDelete = getById(id);
        if (toDelete.getId() != 0)
            er.deleteById(id);
        return toDelete; 
    }
}
