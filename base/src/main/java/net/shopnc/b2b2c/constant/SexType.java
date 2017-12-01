package net.shopnc.b2b2c.constant;

/**
 * Created by zxy on 2015/10/23.
 */
public class SexType {
    /**
     * 保密SECRECY
     */
    public static final int SECRECY = 0;
    /**
     * 男MALE
     */
    public static final int MALE = 1;
    /**
     * 女FEMALE
     */
    public static final int FEMALE = 2;

    /**
     * 获取所有属性值
     * @return
     */
    public Integer[] getAllValues() {
        Integer[] fields = new Integer[3];
        fields[0] = SECRECY;
        fields[1] = MALE;
        fields[2] = FEMALE;
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