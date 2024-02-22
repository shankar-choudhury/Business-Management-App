package com.spring2024project.Scheduler.repository;

import com.spring2024project.Scheduler.entity.ZipCodeData;
import org.springframework.data.repository.CrudRepository;

public interface ZipCodeDataRepository extends CrudRepository<ZipCodeData, String> {
}
