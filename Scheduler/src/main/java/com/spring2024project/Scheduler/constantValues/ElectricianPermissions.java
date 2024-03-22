package com.spring2024project.Scheduler.constantValues;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.spring2024project.Scheduler.constantValues.Permissions.*;
import static com.spring2024project.Scheduler.constantValues.Permissions.INVENTORY_READ;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ElectricianPermissions {
    MASTER(
            USER_NONE,
            CUSTOMER_NONE,
            JOB_READ_UPDATE,
            INVENTORY_READ),
    JOURNEYMAN(
            USER_READ_UPDATE,
            CUSTOMER_NONE,
            JOB_READ_UPDATE,
            INVENTORY_READ),
    APPRENTICE(
            USER_NONE,
            CUSTOMER_NONE,
            JOB_READ,
            INVENTORY_READ);

    private final Permissions user;
    private final Permissions customer;
    private final Permissions job;
    private final Permissions inventory;
}
