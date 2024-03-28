package com.spring2024project.Scheduler.service;

import com.spring2024project.Scheduler.customValidatorTags.ZipCodeValidatorTag;
import com.spring2024project.Scheduler.entity.Address;
import com.spring2024project.Scheduler.entity.CreditCard;
import com.spring2024project.Scheduler.repository.AddressRepository;
import com.spring2024project.Scheduler.repository.CreditCardRepository;
import com.spring2024project.Scheduler.repository.CustomerRepository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.spring2024project.Scheduler.entity.CreditCard.*;

/**
 * This service class provides functionality to perform CRUD operations on CreditCard entities.
 * It implements the BaseService interface.
 * @Author Shankar Choudhury
 * TODO: CC has many-to-one relationship with Address. Implement same functionality as Customer with CC/Address
 */
@Service
@Transactional
public class CreditCardService implements BaseService<CreditCard>{

    private final CreditCardRepository ccr;
    private final ZipCodeValidatorTag zt;
    private final AddressService as;
    private final EntityManager em;

    /**
     * Constructs a CreditCardService instance with the given CreditCardRepository.
     * @param ccr The CreditCardRepository to be used by the service.
     */
    @Autowired
    public CreditCardService(CreditCardRepository ccr,
                             ZipCodeValidatorTag zt,
                             AddressService as,
                             EntityManager em) {
        this.ccr = ccr;
        this.zt = zt;
        this.as = as;
        this.em = em;
    }

    /**
     * Retrieves all credit cards from the database.
     * @return A list of all credit cards.
     */
    @Override
    public List<CreditCard> getAll() {
        return (List<CreditCard>) ccr.findAll();
    }

    /**
     * Retrieves a credit card by its ID from the database.
     * @param id The ID of the credit card to retrieve.
     * @return The credit card with the specified ID, or a default credit card if not found.
     */
    @Override
    public CreditCard getById(int id) {
        return ccr.findById(id).orElse(emptyCreditCard());
    }

    /**
     * Creates a new credit card in the database.
     * @param entity The credit card entity to create.
     * @return The created credit card.
     */
    @Override
    public CreditCard create(CreditCard entity) {
        CreditCard newCard = checkedFrom(entity, zt);
        Address billingAddress = newCard.getBillingAddress();
        if (billingAddress.getId() == 0) {
            var formatted = Address.from(billingAddress, zt);
            newCard.setBillingAddress(formatted);
            // Now save the credit card
            return ccr.save(newCard);
        }
        return CreditCard.emptyCreditCard();
    }

    @Override
    public CreditCard update(int id, CreditCard entity) {
        return null;
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
            toDelete.setBillingAddress(null);
            em.flush();
            ccr.deleteById(id);
            return deleted;
        }
        return toDelete;
    }
}
