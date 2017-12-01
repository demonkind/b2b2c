package net.shopnc.b2b2c.vo.store;

/**
 * 店铺客服Vo
 * Created by dqw on 2016/02/02.
 */
public class ServiceVo {
    /**
     * 客服名称
     */
    private String name;
    /**
     * 客服工具</br>
     * 1-QQ 2-旺旺
     */
    private int type;
    /**
     * 客服账号
     */
    private String num;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "ServiceVo{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", num='" + num + '\'' +
                '}';
    }
}

