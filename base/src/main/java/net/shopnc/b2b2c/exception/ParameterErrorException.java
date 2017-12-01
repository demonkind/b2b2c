package net.shopnc.b2b2c.exception;

/**
 * Created by zxy on 2015-11-25.
 */
public class ParameterErrorException extends ShopException {

    public ParameterErrorException() {
        super("参数错误");
    }
    public ParameterErrorException(String message) {
        super(message);
    }
}