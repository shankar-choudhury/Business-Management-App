package com.spring2024project.Scheduler.controller;

import com.spring2024project.Scheduler.entity.CreditCard;
import com.spring2024project.Scheduler.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/credit-cards")
public class CreditCardController implements BaseController<CreditCard> {

    private final BaseService<CreditCard> creditCardService;

    @Autowired
    public CreditCardController(BaseService<CreditCard> creditCardService) {
        this.creditCardService = creditCardService;
    }

    @Override
    @GetMapping
    public ResponseEntity<List<CreditCard>> getAll() {
        List<CreditCard> creditCards = creditCardService.getAll();
        return new ResponseEntity<>(creditCards, HttpStatus.OK);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<CreditCard> getById(@PathVariable int id) {
        CreditCard creditCard = creditCardService.getById(id);
        return creditCard.getNumber().isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(creditCard, HttpStatus.OK);
    }

    @Override
    @PostMapping
    public ResponseEntity<CreditCard> create(@RequestBody CreditCard creditCard) {
        CreditCard createdCreditCard = creditCardService.create(creditCard);
        return new ResponseEntity<>(createdCreditCard, HttpStatus.CREATED);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<CreditCard> update(@PathVariable int id, @RequestBody CreditCard creditCard) {
        CreditCard updatedCreditCard = creditCardService.update(id, creditCard);
        return updatedCreditCard.getNumber().isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(updatedCreditCard, HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<CreditCard> delete(@PathVariable int id) {
        CreditCard deletedCreditCard = creditCardService.delete(id);
        return deletedCreditCard.getNumber().isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(deletedCreditCard, HttpStatus.OK);
    }

}
