package com.spring2024project.Scheduler.entityFunctionalities.address;

public record AddressDto(String buildingNumber,
                         String street,
                         String city,
                         String state,
                         String zipcode) {
    public AddressDto from(Address address) {
        return new AddressDto(
                address.getBuildingNumber(),
                address.getStreet(),
                address.getCity(),
                address.getState(),
                address.getZipcode());
    }
}
