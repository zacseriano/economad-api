package zacseriano.economadapi.shared.validator.data;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import zacseriano.economadapi.shared.utils.DataUtils;

import java.time.format.DateTimeParseException;

public class StringAsLocalDateValidator implements ConstraintValidator<StringAsLocalDateValid, String> {
    @SuppressWarnings("unused")
	private String value;

    @Override
    public void initialize(StringAsLocalDateValid constraintAnnotation) {
        this.value = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(!value.isBlank()) {
            try {
                DataUtils.stringToLocalDate(value);
            } catch (DateTimeParseException e) {
                return false;
            }
        }

        return true;
    }
}
