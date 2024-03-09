package com.spring2024project.Scheduler.validatingMethods;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;

import static com.spring2024project.Scheduler.exception.ValidationException.Cause.FORMAT;
import static com.spring2024project.Scheduler.validatingMethods.StringValidator.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PersonValidator {
    public enum PersonValidationPattern {
        VALID_NAME_PATTERN("\\p{Alpha}+",
                "Matches a valid name containing alphabetic characters only. Examples: 'John', 'Alice', 'Smith'"),
        VALID_PHONE_NUMBER_PATTERN("^(\\(?(\\d{3})\\)?[-.\\s]?)?\\d{3}[-.\\s]?\\d{4}$",
                "Matches a valid phone number in various formats. Examples: '123-456-7890', '(123) 456 7890', '123.456.7890'");

        private final String regex;
        private final String description;

        PersonValidationPattern(String regex, String description) {
            this.regex = regex;
            this.description = description;
        }

        public String getRegex() {
            return regex;
        }

        public String getDescription() {
            return description;
        }
    }

    public static String correctNameFormat(String name) {
        return validateString(name, string -> string.matches(PersonValidationPattern.VALID_NAME_PATTERN.getRegex()), FORMAT);
    }

    public static String correctEmailFormat(String email) {
        return validateString(email, string -> EmailValidator.getInstance().isValid(string), FORMAT);
    }

    public static String correctPhoneNumberFormat(String phoneNumber) {
        return validateString(phoneNumber, p -> p.matches(PersonValidationPattern.VALID_PHONE_NUMBER_PATTERN.getRegex()), FORMAT);
    }
}
