package net.shopnc.b2b2c.exception;

/**
 * Created by dqw on 2015/12/16.
 */
public class SellerPasswordErrorException extends ShopException {

    public SellerPasswordErrorException() {
        super("商家密码错误");
    }
    public SellerPasswordErrorException(String message) {
        super(message);
    }
}