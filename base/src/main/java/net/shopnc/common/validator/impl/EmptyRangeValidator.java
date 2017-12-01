package net.shopnc.common.validator.impl;

import net.shopnc.common.validator.EmptyRange;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
/**
 * Created by cj on 2015/11/21.
 */
public class EmptyRangeValidator implements ConstraintValidator<EmptyRange, String> {

    private int min;
    private int max;


    public void initialize(EmptyRange emptyRange) {
        // TODO Auto-generated method stub
        this.min = emptyRange.min();
        this.max = emptyRange.max();
    }

    public boolean isValid(String value, ConstraintValidatorContext arg1) {
        // TODO Auto-generated method stub
        if (value == null ||value.equals("")){
            return true;
        }else{
            return value.length() >= this.min && value.length() <= this.max;
        }
    }

}
