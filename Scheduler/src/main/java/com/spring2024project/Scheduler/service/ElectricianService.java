package com.spring2024project.Scheduler.service;

import com.spring2024project.Scheduler.customValidatorTags.ZipCodeValidatorTag;
import com.spring2024project.Scheduler.entity.employees.Electrician;
import com.spring2024project.Scheduler.repository.ElectricianRepository;

import java.util.List;

public class ElectricianService implements BaseService<Electrician> {

    private final ElectricianRepository er;
    private final AddressService as;
    private final ZipCodeValidatorTag zt;

    public ElectricianService(ElectricianRepository er, AddressService as, ZipCodeValidatorTag zt) {
        this.er = er;
        this.as = as;
        this.zt = zt;
    }
    @Override
    public List<Electrician> getAll() {
        return (List<Electrician>) er.findAll();
    }

    @Override
    public Electrician getById(int id) {
        return er.findById(id);
    }

    @Override
    public Electrician create(Electrician entity) {
        return null;
    }

    @Override
    public Electrician update(int id, Electrician entity) {
        return null;
    }

    @Override
    public Electrician delete(int id) {
        return null;
    }
}
