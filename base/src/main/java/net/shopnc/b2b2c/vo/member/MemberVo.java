package net.shopnc.b2b2c.vo.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import net.shopnc.b2b2c.domain.member.Member;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;

/**
 * Created by zxy on 2016-01-22
 */
public class MemberVo {
    /**
     * 会员自增编码
     */
    private int memberId;
    /**
     * 会员名称
     */
    private String memberName = "";
    /**
     * 会员真实姓名
     */
    private String trueName = "";
    /**
     * 登录密码
     */
    @JsonIgnore
    private String memberPwd = "";
    /**
     * 支付密码
     */
    @JsonIgnore
    private String payPwd = "";
    /**
     * 性别
     */
    private int memberSex = 0;
    /**
     * 生日
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    private Timestamp birthday;
    /**
     * 头像
     */
    private String avatar = "";
    /**
     * 邮箱
     */
    private String email = "";
    /**
     * 是否已进行邮箱绑定 0未绑定 1已绑定
     */
    private int emailIsBind = 0;
    /**
     * 手机号
     */
    private String mobile = "";
    /**
     * 是否已进行手机绑定 0未绑定 1已绑定
     */
    private int mobileIsBind = 0;
    /**
     * QQ号码
     */
    private String memberQQ = "";
    /**
     * 旺旺号码
     */
    private String memberWW = "";
    /**
     * 注册时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Timestamp registerTime;
    /**
     * 登录次数
     */
    private int loginNum = 0;
    /**
     * 登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Timestamp loginTime;
    /**
     * 上次登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Timestamp lastLoginTime;
    /**
     * 登录IP
     */
    private String loginIp = "";
    /**
     * 上次登录IP
     */
    private String lastLoginIp = "";
    /**
     * 会员积分
     */
    private int memberPoints = 0;
    /**
     * 预存款可用余额
     */
    private BigDecimal predepositAvailable = new BigDecimal(0);
    /**
     * 预存款冻结金额
     */
    private BigDecimal predepositFreeze  = new BigDecimal(0);
    /**
     * 所在地省份ID
     */
    private int addressProvinceId = 0;
    /**
     * 所在地城市ID
     */
    private int addressCityId = 0;
    /**
     * 所在地地区ID
     */
    private int addressAreaId = 0;
    /**
     * 所在地地区详情
     */
    private String addressAreaInfo = "";
    /**
     * 经验值
     */
    private int experiencePoints = 0;
    /**
     * 会员是否有购买权限 0无权限 1有权限
     */
    private int allowBuy = 1;
    /**
     * 会员是否有发言权限 0无权限 1有权限
     */
    private int allowTalk = 1;
    /**
     * 会员状态
     */
    private int state = 1;
    /**
     * 会员名更改次数
     */
    private int modifyNum = 0;
    /**
     * 头像路径
     */
    private String avatarUrl = "";
    /**
     * 邮箱隐私文本
     */
    private String emailEncrypt = "";
    /**
     * 手机隐私文本
     */
    private String mobileEncrypt = "";
    /**
     * 会员安全级别
     */
    private int securityLevel = 0;
    /**
     * 当前等级
     */
    private HashMap<String, Object> currGrade = new HashMap<String, Object>();

    public MemberVo(Member m) {
        this.setMemberAttribute(m);
    }

    public MemberVo(Member m, HashMap<String, Object> currGrade) {
        this.setMemberAttribute(m);
        this.currGrade = currGrade;
    }
    /**
     * 赋值Member属性
     */
    private void setMemberAttribute(Member m){
        this.memberId = m.getMemberId();
        this.memberName = m.getMemberName();
        this.trueName = m.getTrueName();
        this.memberPwd = m.getMemberPwd();
        this.payPwd = m.getPayPwd();
        this.memberSex = m.getMemberSex();
        this.birthday = m.getBirthday();
        this.avatar = m.getAvatar();
        this.email = m.getEmail();
        this.emailIsBind = m.getEmailIsBind();
        this.mobile = m.getMobile();
        this.mobileIsBind = m.getMobileIsBind();
        this.memberQQ = m.getMemberQQ();
        this.memberWW = m.getMemberWW();
        this.registerTime = m.getRegisterTime();
        this.loginNum = m.getLoginNum();
        this.loginTime = m.getLoginTime();
        this.lastLoginTime = m.getLastLoginTime();
        this.loginIp = m.getLoginIp();
        this.lastLoginIp = m.getLastLoginIp();
        this.memberPoints = m.getMemberPoints();
        this.predepositAvailable = m.getPredepositAvailable();
        this.predepositFreeze  = m.getPredepositFreeze();
        this.addressProvinceId = m.getAddressProvinceId();
        this.addressCityId = m.getAddressCityId();
        this.addressAreaId = m.getAddressAreaId();
        this.addressAreaInfo = m.getAddressAreaInfo();
        this.experiencePoints = m.getExperiencePoints();
        this.allowBuy = m.getAllowBuy();
        this.allowTalk = m.getAllowTalk();
        this.state = m.getState();
        this.modifyNum = m.getModifyNum();
        this.avatarUrl = m.getAvatarUrl();
        this.emailEncrypt = m.getEmailEncrypt();
        this.mobileEncrypt = m.getMobileEncrypt();
        this.securityLevel = m.getSecurityLevel();
    }


    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getMemberPwd() {
        return memberPwd;
    }

    public void setMemberPwd(String memberPwd) {
        this.memberPwd = memberPwd;
    }

    public String getPayPwd() {
        return payPwd;
    }

    public void setPayPwd(String payPwd) {
        this.payPwd = payPwd;
    }

    public int getMemberSex() {
        return memberSex;
    }

    public void setMemberSex(int memberSex) {
        this.memberSex = memberSex;
    }

    public Timestamp getBirthday() {
        return birthday;
    }

    public void setBirthday(Timestamp birthday) {
        this.birthday = birthday;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEmailIsBind() {
        return emailIsBind;
    }

    public void setEmailIsBind(int emailIsBind) {
        this.emailIsBind = emailIsBind;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getMobileIsBind() {
        return mobileIsBind;
    }

    public void setMobileIsBind(int mobileIsBind) {
        this.mobileIsBind = mobileIsBind;
    }

    public String getMemberQQ() {
        return memberQQ;
    }

    public void setMemberQQ(String memberQQ) {
        this.memberQQ = memberQQ;
    }

    public String getMemberWW() {
        return memberWW;
    }

    public void setMemberWW(String memberWW) {
        this.memberWW = memberWW;
    }

    public Timestamp getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Timestamp registerTime) {
        this.registerTime = registerTime;
    }

    public int getLoginNum() {
        return loginNum;
    }

    public void setLoginNum(int loginNum) {
        this.loginNum = loginNum;
    }

    public Timestamp getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Timestamp loginTime) {
        this.loginTime = loginTime;
    }

    public Timestamp getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Timestamp lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public int getMemberPoints() {
        return memberPoints;
    }

    public void setMemberPoints(int memberPoints) {
        this.memberPoints = memberPoints;
    }

    public BigDecimal getPredepositAvailable() {
        return predepositAvailable;
    }

    public void setPredepositAvailable(BigDecimal predepositAvailable) {
        this.predepositAvailable = predepositAvailable;
    }

    public BigDecimal getPredepositFreeze() {
        return predepositFreeze;
    }

    public void setPredepositFreeze(BigDecimal predepositFreeze) {
        this.predepositFreeze = predepositFreeze;
    }

    public int getAddressProvinceId() {
        return addressProvinceId;
    }

    public void setAddressProvinceId(int addressProvinceId) {
        this.addressProvinceId = addressProvinceId;
    }

    public int getAddressCityId() {
        return addressCityId;
    }

    public void setAddressCityId(int addressCityId) {
        this.addressCityId = addressCityId;
    }

    public int getAddressAreaId() {
        return addressAreaId;
    }

    public void setAddressAreaId(int addressAreaId) {
        this.addressAreaId = addressAreaId;
    }

    public String getAddressAreaInfo() {
        return addressAreaInfo;
    }

    public void setAddressAreaInfo(String addressAreaInfo) {
        this.addressAreaInfo = addressAreaInfo;
    }

    public int getExperiencePoints() {
        return experiencePoints;
    }

    public void setExperiencePoints(int experiencePoints) {
        this.experiencePoints = experiencePoints;
    }

    public int getAllowBuy() {
        return allowBuy;
    }

    public void setAllowBuy(int allowBuy) {
        this.allowBuy = allowBuy;
    }

    public int getAllowTalk() {
        return allowTalk;
    }

    public void setAllowTalk(int allowTalk) {
        this.allowTalk = allowTalk;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getModifyNum() {
        return modifyNum;
    }

    public void setModifyNum(int modifyNum) {
        this.modifyNum = modifyNum;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getEmailEncrypt() {
        return emailEncrypt;
    }

    public void setEmailEncrypt(String emailEncrypt) {
        this.emailEncrypt = emailEncrypt;
    }

    public String getMobileEncrypt() {
        return mobileEncrypt;
    }

    public void setMobileEncrypt(String mobileEncrypt) {
        this.mobileEncrypt = mobileEncrypt;
    }

    public int getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(int securityLevel) {
        this.securityLevel = securityLevel;
    }

    public HashMap<String, Object> getCurrGrade() {
        return currGrade;
    }

    public void setCurrGrade(HashMap<String, Object> currGrade) {
        this.currGrade = currGrade;
    }
}
