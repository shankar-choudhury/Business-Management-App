package com.spring2024project.Scheduler.service;

import com.spring2024project.Scheduler.entity.Address;
import com.spring2024project.Scheduler.entity.Customer;

import java.util.List;

public interface AddressService {
    List<Address> getAllAddresses();
    Address getAddressById(int id);
    Address createAddress(int buildingNumber, String street, String city, String state, int zipcode);
    Address updateAddress(int id, int buildingNumber, String street, String city, String state, int zipcode);
    Address deleteAddress(int id);
}
