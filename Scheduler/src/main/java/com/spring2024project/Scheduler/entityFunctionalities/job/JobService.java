package com.spring2024project.Scheduler.entityFunctionalities.job;

import com.spring2024project.Scheduler.entityFunctionalities.address.AddressService;
import com.spring2024project.Scheduler.entityFunctionalities.generator.GeneratorService;
import com.spring2024project.Scheduler.entityFunctionalities.people.customer.CustomerService;
import com.spring2024project.Scheduler.entityFunctionalities.people.electrician.ElectricianService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class JobService {

    private final JobRepository jr;
    private final AddressService as;
    private final CustomerService cs;
    private final ElectricianService es;
    private final GeneratorService gs;

    @Autowired
    public JobService(JobRepository jr,
                      AddressService as,
                      CustomerService cs,
                      ElectricianService es,
                      GeneratorService gs) {
        this.jr = jr;
        this.as = as;
        this.cs = cs;
        this.es = es;
        this.gs = gs;
    }

    public List<Job> getAll() {
        return (List<Job>) jr.findAll();
    }

    public Job getById(int id) {
        return jr.findById(id).orElse(Job.emptyJob());
    }

    public Job create(JobDto entity) {
        try {
            var jobEntity = Job.builder()
                    .customer(cs.getById(entity.customerId()))
                    .electricians(es.getAllById(entity.electricianIds()))
                    .startDate(entity.startDate())
                    .endDate(entity.endDate())
                    .jobAddress(as.getById(entity.addressId()))
                    .isComplete(entity.isComplete())
                    .build();
            jobEntity.getCustomer().setGenerators(gs.getAllById(entity.generatorIds()));
            gs.getAllById(entity.generatorIds()).forEach(generator -> generator.setAssigned(true));
            var newJob = Job.from(jobEntity);
            return jr.save(newJob);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Job", e);
        }
    }

    public Job update(int id, JobDto entity) {
        var jobToUpdate = jr.findById(id).orElseThrow(() -> new RuntimeException("Job not found with id: " + id));
        jobToUpdate.setElectricians(es.getAllById(entity.electricianIds()));
        jobToUpdate.setStartDate(entity.startDate());
        jobToUpdate.setEndDate(entity.endDate());
        jobToUpdate.setJobAddress(as.getById(entity.addressId()));
        jobToUpdate.setComplete(entity.isComplete());
        return jr.save(jobToUpdate);
    }

    public Job delete(int id) {
        return null;
    }
}
