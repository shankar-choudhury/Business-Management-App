package com.spring2024project.Scheduler.entityFunctionalities.job;

import com.spring2024project.Scheduler.entityFunctionalities.address.AddressDto;

public record JobFetchDto(JobCreationDto jobCreationDto, AddressDto jobAddressDto) {}
