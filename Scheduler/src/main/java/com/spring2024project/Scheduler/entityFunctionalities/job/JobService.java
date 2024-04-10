package com.spring2024project.Scheduler.entityFunctionalities.job;

import com.spring2024project.Scheduler.entityFunctionalities.address.AddressService;
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

    @Autowired
    public JobService(JobRepository jr,
                      AddressService as,
                      CustomerService cs,
                      ElectricianService es) {
        this.jr = jr;
        this.as = as;
        this.cs = cs;
        this.es = es;
    }

    public List<Job> getAll() {
        return (List<Job>) jr.findAll();
    }

    public Job getById(int id) {
        return jr.findById(id).orElse(Job.emptyJob());
    }

    public Job create(JobCreationDto entity) {
        try {
            var jobEntity = Job.builder()
                    .customer(cs.getById(entity.customerId()))
                    .electricians(es.getAllById(entity.electricianIds()))
                    .startDate(entity.startDate())
                    .endDate(entity.endDate())
                    .jobAddress(as.getById(entity.addressId()))
                    .isComplete(entity.isComplete())
                    .build();
            var newJob = Job.from(jobEntity);
            return jr.save(newJob);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Job", e);
        }
    }

    public Job update(int id, JobCreationDto entity) {
        return null;
    }

    public Job delete(int id) {
        return null;
    }
}
