package com.spring2024project.Scheduler.service;

import com.spring2024project.Scheduler.entity.Generator;

import java.util.List;

public interface GeneratorService {
    List<Generator> getAllGenerators();
    Generator getGeneratorById(int id);
    Generator createGenerator(String manufacturer, int kWSize, String installInstructions);
    Generator updateGenerator(int id, String manufacturer, int kWSize, String installInstructions);
    Generator deleteGenerator(int id);
}
