package com.spring2024project.Scheduler.service;

import com.spring2024project.Scheduler.entity.Generator;
import com.spring2024project.Scheduler.repository.GeneratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.spring2024project.Scheduler.entity.Generator.*;

import java.util.List;

@Service
public class generatorServiceImp implements GeneratorService{
    private GeneratorRepository gr;

    @Autowired
    public generatorServiceImp(GeneratorRepository gr) {this.gr = gr;}

    @Override
    public List<Generator> getAllGenerators() {
        return (List<Generator>) gr.findAll();
    }

    @Override
    public Generator getGeneratorById(int id) {
        return gr.findById(id).orElse(defaultGenerator());
    }

    @Override
    public Generator createGenerator(String manufacturer, int kWSize, String installInstructions) {
        return gr.save(of(manufacturer, kWSize, installInstructions));
    }

    @Override
    public Generator updateGenerator(int id, String manufacturer, int kWSize, String installInstructions) {
        Generator toUpdate = getGeneratorById(id);
        if (toUpdate.getkWSize() != 0) {
            toUpdate.setManufacturer(manufacturer);
            toUpdate.setkWSize(kWSize);
            toUpdate.setInstallInstructions(installInstructions);
            gr.save(toUpdate);
        }
        return toUpdate;
    }

    @Override
    public Generator deleteGenerator(int id) {
        Generator toDelete = getGeneratorById(id);
        if (toDelete.getkWSize() != 0) {
            Generator save = from(toDelete);
            gr.deleteById(id);
            return save;
        }
        return toDelete;
    }
}
