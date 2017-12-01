package net.shopnc.b2b2c.constant;

/**
 * 短信验证码发送类型
 * Created by zxy on 2015/10/23
 */
public class SmsCodeSendType {
    /**
     * 注册REGISTER
     */
    public static final int REGISTER = 1;
    /**
     * 登录LOGIN
     */
    public static final int LOGIN = 2;
    /**
     * 找回密码FINDPASSWORD
     */
    public static final int FINDPASSWORD = 3;
    /**
     * 绑定手机MOBILEBIND
     */
    public static final int MOBILEBIND = 4;
    /**
     * 手机认证
     */
    public static final int MOBILEAUTH = 5;
    /**
     * 获取所有属性值
     * @return
     */
    public Integer[] getAllValues() {
        Integer[] fields = new Integer[5];
        fields[0] = REGISTER;
        fields[1] = LOGIN;
        fields[2] = FINDPASSWORD;
        fields[3] = MOBILEBIND;
        fields[4] = MOBILEAUTH;
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
