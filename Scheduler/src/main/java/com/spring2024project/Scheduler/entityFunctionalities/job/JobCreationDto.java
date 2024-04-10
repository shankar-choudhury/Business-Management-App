package com.spring2024project.Scheduler.entityFunctionalities.job;

import java.time.LocalDate;
import java.util.List;

record JobCreationDto(int customerId,
                      List<Integer> electricianIds,
                      LocalDate startDate,
                      LocalDate endDate,
                      int addressId,
                      boolean isComplete) {}
