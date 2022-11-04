package zacseriano.economadapi.shared.validator.data;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StringAsLocalDateValidator.class)
public @interface StringAsLocalDateValid {
    String message() default "Formato de data inválida, informe o padrão dd/MM/yyyy";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String value() default "";
}
