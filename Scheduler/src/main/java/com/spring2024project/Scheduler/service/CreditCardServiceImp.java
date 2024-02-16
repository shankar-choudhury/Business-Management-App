package com.spring2024project.Scheduler.service;

import com.spring2024project.Scheduler.entity.Address;
import com.spring2024project.Scheduler.entity.CreditCard;
import com.spring2024project.Scheduler.repository.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.spring2024project.Scheduler.entity.CreditCard.*;

@Service
public class CreditCardServiceImp implements CreditCardService{
    private CreditCardRepository cr;

    @Autowired
    public CreditCardServiceImp(CreditCardRepository cr) {
        this.cr = cr;
    }
    @Override
    public List<CreditCard> getAllCards() {
        return (List<CreditCard>) cr.findAll();
    }

    @Override
    public CreditCard createCard(long number, int expMonth, int expYear) {
        return cr.save(of(number, expMonth, expYear));
    }

    @Override
    public CreditCard getCardById(int id) {
        return cr.findById(id).orElse(defaultCC());
    }

    @Override
    public CreditCard updateCard(int id, long newNumber, int expMonth, int expYear) {
        CreditCard toUpdate = getCardById(id);
        if (toUpdate.getId() != 0) {
            toUpdate.setNumber(newNumber);
            toUpdate.setExpMonth(expMonth);
            toUpdate.setExpYear(expYear);;
            return cr.save(toUpdate);
        }
        return toUpdate;
    }

    @Override
    public CreditCard deleteCard(int id) {
        CreditCard toDelete = getCardById(id);
        if (toDelete.getId() != 0) {
            CreditCard deleted = from(toDelete);
            cr.deleteById(id);
            return deleted;
        }
        return toDelete;
    }
}
