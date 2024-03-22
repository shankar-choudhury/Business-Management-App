package com.spring2024project.Scheduler.entity.employees;

import com.spring2024project.Scheduler.constantValues.OfficeAccessPermissions;

public class Administrator extends Employee {
    private final OfficeAccessPermissions permissions = OfficeAccessPermissions.ADMINISTRATOR;
}
