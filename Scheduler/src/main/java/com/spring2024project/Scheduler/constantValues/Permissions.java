package com.spring2024project.Scheduler.constantValues;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
enum Permissions {
    USER_ALL(true, true, true, true),
    USER_READ_UPDATE(true, true, false, false),
    USER_READ(true, false, false, false),
    USER_NONE(false, false, false, false),

    CUSTOMER_ALL(true, true, true, true),
    CUSTOMER_READ_UPDATE(false, true, true, false),
    CUSTOMER_READ(false, true, false, false),
    CUSTOMER_NONE(false, false, false, false),

    JOB_ALL(true, true, true, true),
    JOB_READ_UPDATE(true, true, false, false),
    JOB_READ(true, false, false, false),
    JOB_NONE(false, false, false, false),

    INVENTORY_ALL(true, true, true, true),
    INVENTORY_READ(true, false, false, false),
    INVENTORY_NONE(false, false, false, false);

    private final boolean canCreate;
    private final boolean canRead;
    private final boolean canUpdate;
    private final boolean canDelete;
}
