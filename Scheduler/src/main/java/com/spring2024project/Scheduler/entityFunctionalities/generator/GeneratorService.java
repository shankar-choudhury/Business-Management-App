package com.spring2024project.Scheduler.entityFunctionalities.generator;

import com.spring2024project.Scheduler.entityFunctionalities.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.spring2024project.Scheduler.entityFunctionalities.generator.Generator.*;

import java.util.List;

/**
 * Service class for managing generators.
 * @Author Shankar Choudhury
 */
@Service
public class GeneratorService implements BaseService<Generator> {
    private GeneratorRepository gr;

    /**
     * Constructs a GeneratorService with the given GeneratorRepository.
     * @param gr The GeneratorRepository to be used by the service.
     */
    @Autowired
    public GeneratorService(GeneratorRepository gr) {
        this.gr = gr;
    }

    /**
     * Retrieves all generators from the database.
     * @return A list of all generators.
     */
    @Override
    public List<Generator> getAll() {
        return (List<Generator>) gr.findAll();
    }

    public List<Generator> getAllById(List<Integer> ids) {return (List<Generator>) gr.findAllById(ids);}

    /**
     * Retrieves a generator by its ID from the database.
     * @param id The ID of the generator to retrieve.
     * @return The generator with the specified ID, or a default generator if not found.
     */
    @Override
    public Generator getById(int id) {
        return gr.findById(id).orElse(defaultGenerator());
    }

    /**
     * Creates a new generator in the database.
     * @param generator The generator to create.
     * @return The created generator.
     */
    @Override
    public Generator create(Generator generator) {
        return gr.save(from(generator));
    }

    /**
     * Updates an existing generator in the database.
     * @param id The ID of the generator to update.
     * @param generator The updated generator entity.
     * @return The updated generator.
     */
    @Override
    public Generator update(int id, Generator generator) {
        Generator toUpdate = getById(id);
        if (toUpdate.getId() != 0) {
            toUpdate = from(generator);
            gr.save(toUpdate);
        }
        return toUpdate;
    }

    /**
     * Deletes a generator from the database.
     * @param id The ID of the generator to delete.
     * @return The deleted generator, or a default generator if not found.
     */
    @Override
    public Generator delete(int id) {
        Generator toDelete = getById(id);
        if (toDelete.getId() != 0) {
            gr.deleteById(id);
            return toDelete;
        }
        return toDelete;
    }
}
