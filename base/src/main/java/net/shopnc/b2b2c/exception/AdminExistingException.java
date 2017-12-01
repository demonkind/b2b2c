package net.shopnc.b2b2c.exception;

/**
 * Created by dqw on 2015-12-30.
 */
public class AdminExistingException extends ShopException {

    public AdminExistingException() {
        super("管理员已存在");
    }
    public AdminExistingException(String message) {
        super(message);
    }
}