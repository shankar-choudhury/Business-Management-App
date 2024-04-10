package com.spring2024project.Scheduler.entityFunctionalities.people.employees;

import com.spring2024project.Scheduler.constantValues.OfficeAccessPermissions;
import com.spring2024project.Scheduler.entityFunctionalities.people.Employee;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LogisticalManager extends Employee {
    public static final OfficeAccessPermissions permissions = OfficeAccessPermissions.LOGISTICAL_MANAGER;
}
