package net.shopnc.b2b2c.vo;

/**
 * 商品筛选已选项VO
 * Created by shopnc.feng on 2016-01-07.
 */
public class SearchCheckedFilterVo {
    /**
     * 名称
     */
    private String name;
    /**
     * 值
     */
    private String value;
    /**
     * 参数
     */
    private String param;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return "SearchCheckedFilterVo{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", param='" + param + '\'' +
                '}';
    }
}
