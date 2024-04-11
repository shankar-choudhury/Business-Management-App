package com.spring2024project.Scheduler.entityFunctionalities.people.electrician;

import com.spring2024project.Scheduler.entityFunctionalities.address.AddressDto;
import com.spring2024project.Scheduler.entityFunctionalities.address.AddressService;
import com.spring2024project.Scheduler.entityFunctionalities.job.Job;
import com.spring2024project.Scheduler.entityFunctionalities.job.JobService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ElectricianService {

    private final ElectricianRepository er;
    private final AddressService as;

    @Autowired
    public ElectricianService(ElectricianRepository er, AddressService as) {
        this.er = er;
        this.as = as;
    }

    public List<Electrician> getAll() {
        return (List<Electrician>) er.findAll();
    }

    public Electrician getById(int id) {
        return er.findById(id)
                .orElseThrow(() -> new RuntimeException("Electrician not found with id: " + id));
    }

    public List<Electrician> getAllById(List<Integer> ids) {return (List<Electrician>)er.findAllById(ids);}

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

    public Electrician updatePersonalDetails(int id, ElectricianPersonalDetailsDto personalDetailsDto) {
        var toUpdate = getById(id);
        toUpdate.setSalary(personalDetailsDto.salary());
        toUpdate.setFirstName(personalDetailsDto.firstName());
        toUpdate.setLastName(personalDetailsDto.lastName());
        toUpdate.setPhoneNumber(personalDetailsDto.phoneNumber());
        toUpdate.setEmail(personalDetailsDto.email());
        return er.save(toUpdate);
    }

    public Electrician updateHomeAddress(int id, AddressDto addressDto) {
        var addressToUpdate = as.getById(addressDto.id());
        if (addressToUpdate.getId() != 0) {
            addressToUpdate.setBuildingNumber(addressDto.buildingNumber());
            addressToUpdate.setStreet(addressDto.street());
            addressToUpdate.setCity(addressDto.city());
            addressToUpdate.setState(addressDto.state());
            addressToUpdate.setZipcode(addressDto.zipcode());
            as.updateElectricianAddress(addressToUpdate.getId(), addressToUpdate);
            return getById(id);
        }
        return Electrician.emptyElectrician();
    }

    public Electrician delete(int id) {
        Electrician toDelete = getById(id);
        if (toDelete.getId() != 0)
            er.deleteById(id);
        return toDelete;
    }
}
