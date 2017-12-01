package net.shopnc.common.validator.impl;

import net.shopnc.common.validator.Mobile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * Created by dqw on 2015/10/23.
 */
public class MobileValidator implements ConstraintValidator<Mobile, String> {

    private String mobileReg = "^1\\d{10}$";//表示金额的正则表达式
    private Pattern mobileyPattern = Pattern.compile(mobileReg);

    public void initialize(Mobile mobile) {
        // TODO Auto-generated method stub

    }

    public boolean isValid(String value, ConstraintValidatorContext arg1) {
        // TODO Auto-generated method stub
        if (value == null)
            return true;
        return mobileyPattern.matcher(value.toString()).matches();
    }

}
