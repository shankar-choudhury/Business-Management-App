package com.spring2024project.Scheduler.controller;

import com.spring2024project.Scheduler.entity.CreditCard;
import com.spring2024project.Scheduler.service.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/credit-cards")
public class CreditCardController {

    private final CreditCardService creditCardService;

    @Autowired
    public CreditCardController(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    @GetMapping
    public ResponseEntity<List<CreditCard>> getAllCreditCards() {
        List<CreditCard> creditCards = creditCardService.getAllCards();
        return new ResponseEntity<>(creditCards, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreditCard> getCreditCardById(@PathVariable int id) {
        CreditCard creditCard = creditCardService.getCardById(id);
        return creditCard.getNumber() == 0 ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(creditCard, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CreditCard> createCreditCard(@RequestBody CreditCard creditCard) {
        CreditCard createdCreditCard = creditCardService.createCard(creditCard.getNumber(), creditCard.getExpMonth(), creditCard.getExpYear());
        return new ResponseEntity<>(createdCreditCard, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreditCard> updateCreditCard(@PathVariable int id, @RequestBody CreditCard creditCard) {
        CreditCard updatedCreditCard = creditCardService.updateCard(id, creditCard.getNumber(), creditCard.getExpMonth(), creditCard.getExpYear());
        return updatedCreditCard.getNumber() == 0 ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(updatedCreditCard, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CreditCard> deleteCreditCard(@PathVariable int id) {
        CreditCard deletedCreditCard = creditCardService.deleteCard(id);
        return deletedCreditCard.getNumber() == 0 ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(deletedCreditCard, HttpStatus.OK);
    }
}
