package net.shopnc.common.entity;

import java.util.List;

/**
 * 通用返回结果对象
 */
public class ResultEntity {
    public static final int SUCCESS = 200;
    public static final int FAIL = 400;

    private int code;
    private String message = "";
    private String url = "";
    private Object data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
