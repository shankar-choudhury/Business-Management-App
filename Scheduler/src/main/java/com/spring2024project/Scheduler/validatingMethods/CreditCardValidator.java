package com.spring2024project.Scheduler.validatingMethods;

import java.time.Year;

import static com.spring2024project.Scheduler.exception.ValidationException.Cause.FORMAT;
import static com.spring2024project.Scheduler.validatingMethods.StringValidator.validateString;

public class CreditCardValidator {
    /**
     * Regex pattern for validating credit card numbers.
     * <p>
     * This pattern matches valid credit card numbers, including Visa, Mastercard, American Express, Discover, and Diners Club.
     * Examples of valid credit card numbers include:
     * <ul>
     *     <li>Visa: 4111111111111111</li>
     *     <li>Mastercard: 5555555555554444</li>
     *     <li>American Express: 378282246310005</li>
     *     <li>Discover: 6011111111111117</li>
     *     <li>Diners Club: 30569309025904</li>
     * </ul>
     * </p>
     */
    static final String VALID_CC_NUMBER_PATTERN = "^(?:4[0-9]{12}(?:[0-9]{3})?|[25][1-7][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\\d{3})\\d{11})$";

    /**
     * Verifies that the given month is valid (between 1 and 12).
     * @param month The month to validate.
     * @return The input month if it is valid.
     * @throws IllegalArgumentException if the month is not valid.
     */
    public static Integer verifyMonth(int month) {
        return GeneralValidator.validInt(month, val -> val > 0 && val < 13);
    }

    /**
     * Verifies that the given year is within a specified range.
     * @param year The year to validate.
     * @param additionalYears The additional years to add to the current year for the upper bound of the range.
     * @return The input year if it is within the specified range.
     * @throws IllegalArgumentException if the year is not within the specified range.
     */
    public static Integer verifyYearInRange(int year, int additionalYears) {
        int currentYear = Year.now().getValue();
        return GeneralValidator.validInt(year, val -> val >= currentYear && val <= currentYear + additionalYears);
    }

    /**
     * Verifies that the given credit card number is a valid number that maps to a credit card company with the correct format.
     * @param creditCardNumber Credit card number to validate
     * @return The input credit card if it matches the correct pattern
     * @throws IllegalArgumentException if the credit card number is not formatted properly
     */
    public static String correctCCNumberFormat(String creditCardNumber) {
        return validateString(creditCardNumber,
                string -> string.matches(VALID_CC_NUMBER_PATTERN),
                FORMAT);
    }
}
