package com.spring2024project.Scheduler.entity.employees;

import com.spring2024project.Scheduler.constantValues.OfficeAccessPermissions;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LogisticalManager extends Employee {
    public static final OfficeAccessPermissions permissions = OfficeAccessPermissions.LOGISTICAL_MANAGER;
}
