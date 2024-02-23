package com.spring2024project.Scheduler.customValidatorTags;

import com.spring2024project.Scheduler.repository.ZipCodeDataRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class ZipCodeValidator implements ConstraintValidator<ValidZipCode, String> {
    private final ZipCodeDataRepository repository;
    @Autowired
    public ZipCodeValidator(ZipCodeDataRepository repository) {
        this.repository = repository;
    }
    @Override
    public boolean isValid(String zipCode, ConstraintValidatorContext context) {
        return Objects.nonNull(zipCode) && repository.existsById(zipCode);
    }

    public boolean isMatchingCityAndState(String zipCode, String city, String state) {
        return repository.findById(zipCode)
                .filter(zip -> zip.getPrimaryCity().equals(city) && zip.getState().equals(state))
                .isPresent();
    }
}
