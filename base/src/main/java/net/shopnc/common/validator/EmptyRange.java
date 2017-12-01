package net.shopnc.common.validator;

import net.shopnc.common.validator.impl.EmptyRangeValidator;
import net.shopnc.common.validator.impl.MobileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by cj on 2015/11/21.
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=EmptyRangeValidator.class)
public @interface EmptyRange {

    String message() default"";

    int min() default 0;

    int max() default 2147483647;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
