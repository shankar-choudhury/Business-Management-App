package com.spring2024project.Scheduler.validatingMethods;

import com.spring2024project.Scheduler.entity.Address;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmployeeValidator {
    // validate salary
    public static int validSalary(int salary) {
        return GeneralValidator.validInt(salary, earnings -> earnings > 0);
    }

    public static void unassociatedAddress(Address address) {
        if (Objects.nonNull(address.getCustomer()))
            throw new IllegalArgumentException("Address must not be associated with any Customer");
    }
}
