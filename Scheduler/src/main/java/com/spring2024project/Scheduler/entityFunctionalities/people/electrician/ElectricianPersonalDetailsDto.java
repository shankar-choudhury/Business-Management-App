package com.spring2024project.Scheduler.entityFunctionalities.people.electrician;

record ElectricianPersonalDetailsDto(
        int salary,
        String firstName,
        String lastName,
        String phoneNumber,
        String email){
    public static ElectricianPersonalDetailsDto from (Electrician electrician) {
        return new ElectricianPersonalDetailsDto(
                electrician.getSalary(),
                electrician.getFirstName(),
                electrician.getLastName(),
                electrician.getPhoneNumber(),
                electrician.getEmail());
    }
}
