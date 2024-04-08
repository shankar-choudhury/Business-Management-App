package com.spring2024project.Scheduler.validatingMethods;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmployeeValidator {
    // validate salary
    public static int validSalary(int salary) {
        return GeneralValidator.validInt(salary, earnings -> earnings > 0);
    }
}
