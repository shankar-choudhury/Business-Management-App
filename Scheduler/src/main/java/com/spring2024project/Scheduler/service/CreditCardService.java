package com.spring2024project.Scheduler.service;

import java.util.*;

import com.spring2024project.Scheduler.entity.Address;
import com.spring2024project.Scheduler.entity.CreditCard;

public interface CreditCardService {
    List<CreditCard>  getAllCards();
    CreditCard createCard(long number, int expMonth, int expYear);
    CreditCard getCardById(int id);
    CreditCard updateCard(int id, long newNumber, int expMonth, int expYear);
    CreditCard deleteCard(int id);
}
