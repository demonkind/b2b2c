package net.shopnc.b2b2c.seller.shiro;

import org.apache.shiro.authc.AuthenticationException;

/**
 * Created by dqw on 2015/11/13.
 */
public class CaptchaException extends AuthenticationException {
    public CaptchaException() {
        super();
    }

    public CaptchaException(String message, Throwable cause) {
        super(message, cause);
    }

    public CaptchaException(String message) {
        super(message);
    }

    public CaptchaException(Throwable cause) {
        super(cause);
    }
}
