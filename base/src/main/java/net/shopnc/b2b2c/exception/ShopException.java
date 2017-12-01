package net.shopnc.b2b2c.exception;

/**
 * Created by zxy on 2015-11-06.
 */
public class ShopException extends Exception {
    /**
     * 错误描述信息
     */
    private String errorMsg;
    private Object extendData;

    public ShopException() {
        super();
    }

    public ShopException(String message) {
        super();
        this.errorMsg = message;
    }
    public ShopException(String message,Object extendData) {
        super();
        this.errorMsg = message;
        this.extendData = extendData;
    }
    public String getMessage() {
        return errorMsg;
    }

    public void setMessage(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Object getExtendData() {
        return extendData;
    }

    public void setExtendData(Object extendData) {
        this.extendData = extendData;
    }
}