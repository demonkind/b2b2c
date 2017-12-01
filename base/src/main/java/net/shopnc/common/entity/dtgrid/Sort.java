package net.shopnc.common.entity.dtgrid;

/**
 * 接收dtGrid排序参数
 * Created by shopnc on 2015/11/4.
 */
public class Sort {
    /**
     * 要排序的字段
     */
    private String field;

    /**
     * 排序逻辑 1升序，2降序
     */
    private String logic;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getLogic() {
        return logic;
    }

    public void setLogic(String logic) {
        this.logic = logic;
    }
}
