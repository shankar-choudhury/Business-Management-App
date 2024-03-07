package com.spring2024project.Scheduler.customValidatorTags;

import com.spring2024project.Scheduler.entity.Address;
import com.spring2024project.Scheduler.entity.ZipCodeData;
import com.spring2024project.Scheduler.repository.ZipCodeDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

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
@Component
public class ZipCodeValidatorTag implements ConstraintValidator<ValidZipCode, String> {

    private final ZipCodeDataRepository repository;

    /**
     * Constructs a new ZipCodeValidator with the provided ZipCodeDataRepository.
     * @param repository The repository used to access zip code data.
     */
    @Autowired
    public ZipCodeValidatorTag(ZipCodeDataRepository repository) {
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
     * Checks if the provided zip code exists if it matches the given city and state.
     * @param addressToCheck Address to check for existing and valid zip code
     * @return true if the zip code matches the city and state, false otherwise.
     */
    public boolean isValidAddress(Address addressToCheck) {
        verifyNonNullEmptyOrBlank(
                addressToCheck.getZipcode(),
                addressToCheck.getCity(),
                addressToCheck.getState());
        String toCheckZipCode = addressToCheck.getZipcode();
        String toCheckCity = addressToCheck.getCity();
        return repository.findById(toCheckZipCode)
                .filter(zip -> zip.getAcceptableCities().contains(toCheckCity)
                        && zip.getState().equals(addressToCheck.getState()))
                .isPresent();
    }

    public CrudRepository<ZipCodeData,String> getRep() {return repository;}

}
