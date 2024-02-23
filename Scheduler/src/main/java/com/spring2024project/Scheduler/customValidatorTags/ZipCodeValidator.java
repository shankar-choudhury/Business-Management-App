package com.spring2024project.Scheduler.customValidatorTags;

import com.spring2024project.Scheduler.repository.ZipCodeDataRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

import static com.spring2024project.Scheduler.validatingMethods.StringValidator.verifyNonNullEmptyOrBlank;

/**
 * A custom validator class to validate zip codes. Primarily used for bean validation with annotation tag
 * This validator checks whether a provided zip code exists in the database and whether it matches a given city and state.
 * See Address::from method for future implementation TODOs
 * @Author Shankar Choudhury
 */
public class ZipCodeValidator implements ConstraintValidator<ValidZipCode, String> {

    private final ZipCodeDataRepository repository;

    /**
     * Constructs a new ZipCodeValidator with the provided ZipCodeDataRepository.
     * @param repository The repository used to access zip code data.
     */
    @Autowired
    public ZipCodeValidator(ZipCodeDataRepository repository) {
        this.repository = repository;
    }

    /**
     * Checks if the given zip code is valid.
     * @param zipCode The zip code to validate.
     * @param context The constraint validator context.
     * @return true if the zip code is valid, false otherwise.
     */
    @Override
    public boolean isValid(String zipCode, ConstraintValidatorContext context) {
        return Objects.nonNull(zipCode) && repository.existsById(zipCode);
    }

    /**
     * Checks if the provided zip code matches the given city and state.
     * @param zipCode The zip code to check.
     * @param city    The city to match.
     * @param state   The state to match.
     * @return true if the zip code matches the city and state, false otherwise.
     */
    public boolean isMatchingCityAndState(String zipCode, String city, String state) {
        verifyNonNullEmptyOrBlank(zipCode, city, state);
        return repository.findById(zipCode)
                .filter(zip -> zip.getPrimaryCity().equals(city) && zip.getState().equals(state))
                .isPresent();
    }
}
