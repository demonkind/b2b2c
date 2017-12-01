package net.shopnc.b2b2c.constant;

/**
 * 邮件验证码发送类型
 * Created by zxy on 2015/12/22
 */
public class EmailCodeSendType {
    /**
     * 邮箱认证
     */
    public static final int EMAILAUTH = 1;
    /**
     * 绑定新邮箱
     */
    public static final int EMAILBIND = 2;
    /**
     * 邮箱找回密码
     */
    public static final int FINDPASSWORD = 3;

    /**
     * 获取所有属性值
     * @return
     */
    public Integer[] getAllValues() {
        Integer[] fields = new Integer[3];
        fields[0] = EMAILAUTH;
        fields[1] = EMAILBIND;
        fields[2] = FINDPASSWORD;
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
