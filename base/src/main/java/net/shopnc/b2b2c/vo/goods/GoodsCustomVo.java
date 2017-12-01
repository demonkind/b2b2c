package net.shopnc.b2b2c.vo.goods;

/**
 * 商品与自定义属性关系Vo
 * Created by shopnc.feng on 2015-10-23.
 */
public class GoodsCustomVo {
    /**
     * 自定义属性编号
     */
    private int customId;
    /**
     * 自定义属性名称
     */
    private String customValue;

    public int getCustomId() {
        return customId;
    }

    public void setCustomId(int customId) {
        this.customId = customId;
    }

    public String getCustomValue() {
        return customValue;
    }

    public void setCustomValue(String customValue) {
        this.customValue = customValue;
    }

    @Override
    public String toString() {
        return "GoodsCustomVo{" +
                "customId=" + customId +
                ", customValue='" + customValue + '\'' +
                '}';
    }
}
