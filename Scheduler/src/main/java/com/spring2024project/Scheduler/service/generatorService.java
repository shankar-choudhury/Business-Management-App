package com.spring2024project.Scheduler.service;

import com.spring2024project.Scheduler.entity.Generator;
import com.spring2024project.Scheduler.repository.GeneratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.spring2024project.Scheduler.entity.Generator.*;

import java.util.List;

@Service
public class generatorService implements BaseService<Generator>{
    private GeneratorRepository gr;

    @Autowired
    public generatorService(GeneratorRepository gr) {this.gr = gr;}

    @Override
    public List<Generator> getAll() {
        return (List<Generator>) gr.findAll();
    }

    @Override
    public Generator getById(int id) {
        return gr.findById(id).orElse(defaultGenerator());
    }

    @Override
    public Generator create(Generator generator) {
        return gr.save(from(generator));
    }

    @Override
    public Generator update(int id, Generator generator) {
        Generator toUpdate = getById(id);
        if (toUpdate.getKWSize() != 0) {
            toUpdate.setManufacturer(generator.getManufacturer());
            toUpdate.setKWSize(generator.getKWSize());
            toUpdate.setInstallInstructions(generator.getInstallInstructions());
            gr.save(toUpdate);
        }
        return toUpdate;
    }

    @Override
    public Generator delete(int id) {
        Generator toDelete = getById(id);
        if (toDelete.getKWSize() != 0) {
            Generator save = from(toDelete);
            gr.deleteById(id);
            return save;
        }
        return toDelete;
    }
}
