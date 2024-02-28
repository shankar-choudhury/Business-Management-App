package com.spring2024project.Scheduler.service;

import com.spring2024project.Scheduler.customValidatorTags.ZipCodeValidator;
import com.spring2024project.Scheduler.entity.CreditCard;
import com.spring2024project.Scheduler.repository.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.spring2024project.Scheduler.entity.CreditCard.*;

/**
 * This service class provides functionality to perform CRUD operations on CreditCard entities.
 * It implements the BaseService interface.
 * @Author Shankar Choudhury
 * TODO: CC has many-to-one relationship with Address. Implement same functionality as Customer with CC/Address
 */
@Service
public class CreditCardService implements BaseService<CreditCard>{
    private CreditCardRepository cr;
    private ZipCodeValidator v;

    /**
     * Constructs a CreditCardService instance with the given CreditCardRepository.
     * @param cr The CreditCardRepository to be used by the service.
     */
    @Autowired
    public CreditCardService(CreditCardRepository cr, ZipCodeValidator v) {
        this.cr = cr; this.v = v;
    }

    /**
     * Retrieves all credit cards from the database.
     * @return A list of all credit cards.
     */
    @Override
    public List<CreditCard> getAll() {
        return (List<CreditCard>) cr.findAll();
    }

    /**
     * Retrieves a credit card by its ID from the database.
     * @param id The ID of the credit card to retrieve.
     * @return The credit card with the specified ID, or a default credit card if not found.
     */
    @Override
    public CreditCard getById(int id) {
        return cr.findById(id).orElse(emptyCreditCard());
    }

    /**
     * Creates a new credit card in the database.
     * @param entity The credit card entity to create.
     * @return The created credit card.
     */
    @Override
    public CreditCard create(CreditCard entity) {
        CreditCard newCard = checkedFrom(entity, v);
        return cr.save(newCard);
    }

    /**
     * Updates an existing credit card in the database.
     * @param id The ID of the credit card to update.
     * @param cc The updated credit card entity.
     * @return The updated credit card.
     */
    @Override
    public CreditCard update(int id, CreditCard cc) {
        CreditCard toUpdate = getById(id);
        if (toUpdate.getId() != 0) {
            toUpdate = from(cc);
            return cr.save(toUpdate);
        }
        return toUpdate;
    }

    /**
     * Deletes a credit card from the database.
     * @param id The ID of the credit card to delete.
     * @return The deleted credit card, or a default credit card if not found.
     */
    @Override
    public CreditCard delete(int id) {
        CreditCard toDelete = getById(id);
        if (toDelete.getId() != 0) {
            CreditCard deleted = fromDeleted(toDelete);
            cr.deleteById(id);
            return deleted;
        }
        return toDelete;
    }
}
