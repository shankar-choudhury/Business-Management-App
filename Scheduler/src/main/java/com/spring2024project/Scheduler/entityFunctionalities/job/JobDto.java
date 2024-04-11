package com.spring2024project.Scheduler.entityFunctionalities.job;

import java.time.LocalDate;
import java.util.List;

record JobDto(int customerId,
              List<Integer> electricianIds,
              List<Integer> generatorIds,
              LocalDate startDate,
              LocalDate endDate,
              int addressId,
              boolean isComplete) {}
