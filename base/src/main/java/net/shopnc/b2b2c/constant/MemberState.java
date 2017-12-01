package net.shopnc.b2b2c.constant;

/**
 * 会员状态
 * Created by zxy on 2015-10-23.
 */
public class MemberState {
    /**
     * 开启OPEN
     */
    public static final int OPEN = 1;
    /**
     * 关闭CLOSE
     */
    public static final int CLOSE = 0;
    /**
     * 获取所有属性值
     * @return
     */
    public Integer[] getAllValues() {
        Integer[] fields = new Integer[2];
        fields[0] = (Integer)CLOSE;
        fields[1] = (Integer)OPEN;
        return fields;
    }

    /**
     * 判断是否存在某个值
     * @param param 需要判断的值
     * @return
     */
    public Boolean isExistValue(Integer param){
        for(Integer v: this.getAllValues()){
            if(v.equals(param)){
                return true;
            }
        }
        return false;
    }
}
