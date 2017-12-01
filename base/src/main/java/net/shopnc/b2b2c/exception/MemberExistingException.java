package net.shopnc.b2b2c.exception;

/**
 * Created by zxy on 2015-11-06.
 */
public class MemberExistingException extends ShopException {

    public MemberExistingException() {
        super("用户信息已存在");
    }
    public MemberExistingException(String message) {
        super(message);
    }
}