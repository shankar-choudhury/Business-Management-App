package com.spring2024project.Scheduler.entity;

import lombok.Getter;

import java.util.List;

@Getter
public class NullCustomer extends Person {
    public static final List<Address> emptyAddressList = List.of();
    public static final List<CreditCard> emptyCardList = List.of();
    private static final NullCustomer instance = new NullCustomer();

    private NullCustomer() {
        super("","","","");
    }

    public static NullCustomer getInstance() {
        return instance;
    }

    public List<Address> getAddressList() {
        return emptyAddressList;
    }

    public List<CreditCard> getCreditCardList() {
        return emptyCardList;
    }
}
