package net.shopnc.b2b2c.domain.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.Common;
import net.shopnc.b2b2c.constant.State;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 会员实体<br>
 * Created by zxy on 2015-10-22.
 */
@Entity
@Table(name = "member")
public class Member implements Serializable {
    /**
     * 会员自增编码
     */
    @Id
    @GeneratedValue
    @Column(name="member_id")
    private int memberId;
    /**
     * 会员名称
     */
    @Size(min = 6, max = 100, message = "用户名长度为6-100个字符")
    @Column(name="member_name", length = 100)
    private String memberName = "";
    /**
     * 会员真实姓名
     */
    @Size(max = 100, message = "真实姓名长度应小于100个字符")
    @Column(name="true_name", length = 100)
    private String trueName = "";
    /**
     * 登录密码
     */
    @JsonIgnore
    @Column(name="member_pwd", length = 500)
    private String memberPwd = "";
    /**
     * 支付密码
     */
    @JsonIgnore
    @Size(max = 50, message = "支付密码长度应小于50个字符")
    @Column(name="pay_pwd", length = 50)
    private String payPwd = "";
    /**
     * 性别
     */
    @Column(name="member_sex")
    private int memberSex = 0;
    /**
     * 生日
     */
    @Past
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    @Column(name="birthday")
    private Timestamp birthday;
    /**
     * 头像
     */
    @Size(max = 100, message = "头像长度应小于100个字")
    @Column(name="avatar", length = 100)
    private String avatar = "";
    /**
     * 邮箱
     */
    @Email
    @Size(max = 100, message = "邮箱长度应小于100个字")
    @Column(name="email", length = 100)
    private String email = "";
    /**
     * 是否已进行邮箱绑定 0未绑定 1已绑定
     */
    @Column(name="email_isbind")
    private int emailIsBind = 0;
    /**
     * 手机号
     */
    @Column(name="mobile", length = 50)
    private String mobile = "";
    /**
     * 是否已进行手机绑定 0未绑定 1已绑定
     */
    @Column(name="mobile_isbind")
    private int mobileIsBind = 0;
    /**
     * QQ号码
     */
    @Size(max = 50, message = "QQ号码长度应小于50个字")
    @Column(name="member_qq", length = 50)
    private String memberQQ = "";
    /**
     * 旺旺号码
     */
    @Size(max = 50, message = "旺旺号码长度应小于100个字")
    @Column(name="member_ww", length = 100)
    private String memberWW = "";
    /**
     * 注册时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    @Column(name="register_time")
    private Timestamp registerTime;
    /**
     * 登录次数
     */
    @Column(name="login_num")
    private int loginNum = 0;
    /**
     * 登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    @Column(name="login_time")
    private Timestamp loginTime;
    /**
     * 上次登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    @Column(name="last_login_time")
    private Timestamp lastLoginTime;
    /**
     * 登录IP
     */
    @Size(max = 50, message = "IP长度应小于50个字")
    @Column(name="login_ip", length = 50)
    private String loginIp = "";
    /**
     * 上次登录IP
     */
    @Size(max = 50, message = "IP长度应小于50个字")
    @Column(name="last_login_ip", length = 50)
    private String lastLoginIp = "";
    /**
     * 会员积分
     */
    @Min(0)
    @Column(name="member_points")
    private int memberPoints = 0;
    /**
     * 预存款可用余额
     */
    @Column(name="predeposit_available")
    private BigDecimal predepositAvailable = new BigDecimal(0);
    /**
     * 预存款冻结金额
     */
    @Column(name="predeposit_freeze")
    private BigDecimal predepositFreeze  = new BigDecimal(0);
    /**
     * 所在地省份ID
     */
    @Min(0)
    @Column(name="address_provinceid")
    private int addressProvinceId = 0;
    /**
     * 所在地城市ID
     */
    @Min(0)
    @Column(name="address_cityid")
    private int addressCityId = 0;
    /**
     * 所在地地区ID
     */
    @Min(0)
    @Column(name="address_areaid")
    private int addressAreaId = 0;
    /**
     * 所在地地区详情
     */
    @Size(max = 500, message = "地区详情应小于500个字")
    @Column(name="address_areainfo", length = 500)
    private String addressAreaInfo = "";
    /**
     * 经验值
     */
    @Min(0)
    @Column(name="experience_points")
    private int experiencePoints = 0;
    /**
     * 会员是否有购买权限 0无权限 1有权限
     */
    @Column(name="allow_buy")
    private int allowBuy = 1;
    /**
     * 会员是否有发言权限 0无权限 1有权限
     */
    @Column(name="allow_talk")
    private int allowTalk = 1;
    /**
     * 会员状态
     */
    @Column(name="state")
    private int state = 1;
    /**
     * 会员名更改次数
     */
    @Column(name="modify_num")
    private int modifyNum = 0;
    /**
     * 头像路径
     */
    @Transient
    private String avatarUrl = "";
    /**
     * 邮箱隐私文本
     */
    @Transient
    private String emailEncrypt = "";
    /**
     * 手机隐私文本
     */
    @Transient
    private String mobileEncrypt = "";
    /**
     * 会员安全级别
     */
    @Transient
    private int securityLevel = 0;

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
        if (avatar == null || avatar.equals("")) {
            avatarUrl = ShopConfig.getPublicRoot() + Common.DEFAULT_AVATAR_URL;
        }else{
            avatarUrl = ShopConfig.getUploadRoot() + avatar;
        }
        return avatarUrl;
    }

    public String getEmailEncrypt() {
        if (email != null && email.length() > 0) {
            String[] emailArr = email.split("@");
            if (emailArr[0].length() > 4) {
                emailEncrypt = emailArr[0].replaceAll("(.{0,4})(.{4})(.*)", "$1****$3")+"@"+emailArr[1];
            }else{
                emailEncrypt = emailArr[0].replaceAll("(.{1})(.{0,4})(.*)", "$1****$3")+"@"+emailArr[1];
            }
        }else{
            emailEncrypt = "";
        }
        return emailEncrypt;
    }

    public String getMobileEncrypt() {
        if (mobile != null && mobile.length() > 0) {
            mobileEncrypt = mobile.replaceAll("([0-9]{3})([0-9]{4})([0-9]{4})","$1****$3");
        }else{
            mobileEncrypt = "";
        }
        return mobileEncrypt;
    }

    public int getSecurityLevel() {
        securityLevel = 0;
        if (emailIsBind == State.YES) {
            securityLevel += 1;
        }
        if (mobileIsBind == State.YES) {
            securityLevel += 1;
        }
        if (payPwd!=null && payPwd.length() >0) {
            securityLevel += 1;
        }
        return securityLevel;
    }
}
