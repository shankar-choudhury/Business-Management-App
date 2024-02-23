package com.spring2024project.Scheduler.controller;

import com.spring2024project.Scheduler.entity.CreditCard;
import com.spring2024project.Scheduler.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing CreditCard entities.
 * @Author Shankar Choudhury
 */
@RestController
@RequestMapping("/credit-cards")
public class CreditCardController implements BaseController<CreditCard> {

    private final BaseService<CreditCard> creditCardService;

    /**
     * Constructor for CreditCardController.
     * @param creditCardService The service responsible for handling CreditCard-related operations.
     */
    @Autowired
    public CreditCardController(BaseService<CreditCard> creditCardService) {
        this.creditCardService = creditCardService;
    }

    /**
     * Retrieve all credit cards.
     * @return ResponseEntity containing a list of CreditCard objects.
     */
    @Override
    @GetMapping
    public ResponseEntity<List<CreditCard>> getAll() {
        List<CreditCard> creditCards = creditCardService.getAll();
        return new ResponseEntity<>(creditCards, HttpStatus.OK);
    }

    /**
     * Retrieve a credit card by its ID.
     * @param id The ID of the credit card to retrieve.
     * @return ResponseEntity containing the retrieved CreditCard object.
     */
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<CreditCard> getById(@PathVariable int id) {
        CreditCard creditCard = creditCardService.getById(id);
        return creditCard.getNumber().isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(creditCard, HttpStatus.OK);
    }

    /**
     * Create a new credit card.
     * @param creditCard The CreditCard object to create.
     * @return ResponseEntity containing the created CreditCard object.
     */
    @Override
    @PostMapping
    public ResponseEntity<CreditCard> create(@RequestBody CreditCard creditCard) {
        CreditCard createdCreditCard = creditCardService.create(creditCard);
        return new ResponseEntity<>(createdCreditCard, HttpStatus.CREATED);
    }

    /**
     * Update an existing credit card.
     * @param id The ID of the credit card to update.
     * @param creditCard The updated CreditCard object.
     * @return ResponseEntity containing the updated CreditCard object.
     */
    @Override
    @PutMapping("/{id}")
    public ResponseEntity<CreditCard> update(@PathVariable int id, @RequestBody CreditCard creditCard) {
        CreditCard updatedCreditCard = creditCardService.update(id, creditCard);
        return updatedCreditCard.getNumber().isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(updatedCreditCard, HttpStatus.OK);
    }

    /**
     * Delete a credit card by its ID.
     * @param id The ID of the credit card to delete.
     * @return ResponseEntity containing the deleted CreditCard object.
     */
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<CreditCard> delete(@PathVariable int id) {
        CreditCard deletedCreditCard = creditCardService.delete(id);
        return deletedCreditCard.getNumber().isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(deletedCreditCard, HttpStatus.OK);
    }

}