package net.shopnc.common.validator;

import net.shopnc.common.validator.impl.MobileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by dqw on 2015/10/23.
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=MobileValidator.class)
public @interface Mobile {

    String message() default"手机号码不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
