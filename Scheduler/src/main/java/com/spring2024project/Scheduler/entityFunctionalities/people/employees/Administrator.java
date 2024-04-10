package com.spring2024project.Scheduler.entityFunctionalities.people.employees;

import com.spring2024project.Scheduler.constantValues.OfficeAccessPermissions;
import com.spring2024project.Scheduler.entityFunctionalities.people.Employee;

public class Administrator extends Employee {
    private final OfficeAccessPermissions permissions = OfficeAccessPermissions.ADMINISTRATOR;
}
