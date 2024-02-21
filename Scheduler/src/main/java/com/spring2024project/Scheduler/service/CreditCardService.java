package com.spring2024project.Scheduler.service;

import com.spring2024project.Scheduler.entity.CreditCard;
import com.spring2024project.Scheduler.repository.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.spring2024project.Scheduler.entity.CreditCard.*;

@Service
public class CreditCardService implements BaseService<CreditCard>{
    private CreditCardRepository cr;

    @Autowired
    public CreditCardService(CreditCardRepository cr) {
        this.cr = cr;
    }
    @Override
    public List<CreditCard> getAll() {
        return (List<CreditCard>) cr.findAll();
    }

    @Override
    public CreditCard getById(int id) {
        return cr.findById(id).orElse(defaultCC());
    }

    @Override
    public CreditCard create(CreditCard entity) {
        CreditCard newCard = from(entity);
        return cr.save(newCard);
    }

    @Override
    public CreditCard update(int id, CreditCard cc) {
        CreditCard toUpdate = getById(id);
        if (toUpdate.getId() != 0) {
            toUpdate = from(cc);
            return cr.save(toUpdate);
        }
        return toUpdate;
    }

    @Override
    public CreditCard delete(int id) {
        CreditCard toDelete = getById(id);
        if (toDelete.getId() != 0) {
            CreditCard deleted = from(toDelete);
            cr.deleteById(id);
            return deleted;
        }
        return toDelete;
    }
}
