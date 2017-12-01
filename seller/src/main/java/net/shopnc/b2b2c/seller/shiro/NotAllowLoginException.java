package net.shopnc.b2b2c.seller.shiro;

import org.apache.shiro.authc.AuthenticationException;

/**
 * Created by dqw on 2015/11/13.
 */
public class NotAllowLoginException extends AuthenticationException {
    public NotAllowLoginException() {
        super();
    }

    public NotAllowLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotAllowLoginException(String message) {
        super(message);
    }

    public NotAllowLoginException(Throwable cause) {
        super(cause);
    }
}
