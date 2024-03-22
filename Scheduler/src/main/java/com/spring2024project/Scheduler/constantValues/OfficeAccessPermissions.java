package com.spring2024project.Scheduler.constantValues;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static com.spring2024project.Scheduler.constantValues.Permissions.*;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum OfficeAccessPermissions {
    ADMINISTRATOR(
            USER_ALL,
            CUSTOMER_ALL,
            JOB_ALL,
            INVENTORY_ALL),
    LOGISTICAL_MANAGER(
            USER_READ,
            CUSTOMER_ALL,
            JOB_ALL,
            INVENTORY_ALL),
    HUMAN_RESOURCES(
            USER_ALL,
            CUSTOMER_NONE,
            JOB_NONE,
            INVENTORY_NONE),
    ACCOUNTANT(
            USER_NONE,
            CUSTOMER_READ,
            JOB_READ,
            INVENTORY_READ
    ),
    ADMIN_ASSISTANT(
            USER_READ_UPDATE,
            CUSTOMER_READ,
            JOB_READ,
            INVENTORY_READ);


    private final Permissions user;
    private final Permissions customer;
    private final Permissions job;
    private final Permissions inventory;
}
