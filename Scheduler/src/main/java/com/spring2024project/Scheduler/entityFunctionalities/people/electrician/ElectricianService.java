package com.spring2024project.Scheduler.entityFunctionalities.people.electrician;

import com.spring2024project.Scheduler.entityFunctionalities.address.AddressService;
import com.spring2024project.Scheduler.entityFunctionalities.BaseService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
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

    public List<Electrician> getAllById(List<Integer> ids) {return (List<Electrician>)er.findAllById(ids);}

    @Override
    public Electrician create(Electrician entity) {
        try {
            var newElectrician = Electrician.from(entity);
            var homeAddress = newElectrician.getHomeAddress();
            as.validateAndNormalizeAddress(homeAddress);
            var created = as.create(homeAddress);
            newElectrician.setHomeAddress(as.getById(created.getId()));

            er.save(newElectrician);

            return newElectrician;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create electrician", e);
        }
    }

    @Override
    public Electrician update(int id, Electrician entity) {
        Electrician original = getById(id);
        if (original.getId() != 0) {
            var updated = Electrician.from(entity);
            updated.setId(original.getId());
            return er.save(updated);
        }
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
