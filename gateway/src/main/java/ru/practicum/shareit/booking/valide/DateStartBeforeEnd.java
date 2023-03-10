package ru.practicum.shareit.booking.valide;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateConstraintValidator.class)
public @interface DateStartBeforeEnd {
    String message() default "{DateStartBeforeEnd}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
