package net.shopnc.b2b2c.service.member;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.sf.json.JSONObject;
import net.shopnc.b2b2c.constant.EmailCodeSendType;
import net.shopnc.b2b2c.constant.MemberState;
import net.shopnc.b2b2c.constant.SexType;
import net.shopnc.b2b2c.constant.SiteTitle;
import net.shopnc.b2b2c.constant.SmsCodeSendType;
import net.shopnc.b2b2c.constant.State;
import net.shopnc.b2b2c.dao.member.CardCouponsDao;
import net.shopnc.b2b2c.dao.member.EmailCodeDao;
import net.shopnc.b2b2c.dao.member.MemberDao;
import net.shopnc.b2b2c.dao.member.MemberTokenDao;
import net.shopnc.b2b2c.dao.member.SmsCodeDao;
import net.shopnc.b2b2c.domain.member.Coupons;
import net.shopnc.b2b2c.domain.member.EmailCode;
import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.domain.member.MemberToken;
import net.shopnc.b2b2c.domain.member.SmsCode;
import net.shopnc.b2b2c.exception.MemberExistingException;
import net.shopnc.b2b2c.exception.ParameterErrorException;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.BaseService;
import net.shopnc.b2b2c.service.SiteService;
import net.shopnc.common.entity.dtgrid.DtGrid;
import net.shopnc.common.util.KmsHelper;
import net.shopnc.common.util.PriceHelper;
import net.shopnc.common.util.ShopHelper;
import net.shopnc.common.util.UtilsHelper;

/**
 * Created by zxy on 2015-11-03.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class MemberService extends BaseService {

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private SmsCodeDao smsCodeDao;
    @Autowired
    private EmailCodeDao emailCodeDao;
    @Autowired
    private MemberTokenDao memberTokenDao;
    @Autowired
    private PointsService pointsService;
    @Autowired
    private ExperienceService experienceService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private CardCouponsDao couponsDao;

    /**
     * 用户名普通注册
     * @param member
     * @return
     * @throws ShopException
     */
    public Member register(Member member) throws ShopException {
        if (member.getMemberName()==null || member.getMemberName().equals("") || member.getEmail()==null || member.getEmail().equals("")) {
            throw new ParameterErrorException();
        }
        //验证用户名是否存在
        if (memberDao.memberNameIsExist(member.getMemberName()) == true) {
            throw new MemberExistingException("用户名已存在");
        }
        //验证邮箱是否存在
        if (memberDao.emailIsExist(member.getEmail()) == true) {
            throw new MemberExistingException("邮箱已存在");
        }
        //密码
        member.setMemberPwd(ShopHelper.getMd5(member.getMemberPwd()));
        Timestamp currTime = new Timestamp(System.currentTimeMillis());
        //注册时间
        member.setRegisterTime(currTime);
        //登录时间
        member.setLoginTime(currTime);
        //登录次数
        member.setLoginNum(1);
        //性别
        member.setMemberSex(SexType.SECRECY);
        //状态
        member.setState(MemberState.OPEN);
        //购买权限
        member.setAllowBuy(State.YES);
        //发言权限
        member.setAllowTalk(State.YES);
        //IP
        member.setLoginIp(ShopHelper.getAddressIP());
        //普通注册用户默认用户名更改次数为1，即不可更改用户名
        member.setModifyNum(1);
        Serializable memberId = memberDao.save(member);
        //会员ID
        member.setMemberId((Integer) memberId);
        //增加积分
        pointsService.addPointsRegister(member.getMemberId());
        //增加经验
        experienceService.addExperienceRegister(member.getMemberId());
        return member;
    }
    /**
     * 手机注册
     * @param member
     * @param authCode
     * @return
     * @throws ShopException
     */
    public Member registerMobile(Member member, String authCode) throws ShopException {
        if (!siteService.getSiteInfo().get(SiteTitle.SMSREGISTER).equals("1")) {
            throw new ShopException("系统未开启手机注册功能");
        }
        if (member.getMobile()==null || member.getMobile().equals("")) {
            throw new ParameterErrorException();
        }
        if(authCode != null){
        	//验证动态码
        	SmsCode smsCode = new SmsCode();
        	smsCode.setSendType(SmsCodeSendType.REGISTER);
        	smsCode.setMobilePhone(member.getMobile());
        	smsCode.setAuthCode(authCode);
        	if (smsCodeDao.checkCode(smsCode).equals(false)) {
        		throw new ShopException("动态码错误或已过期，重新输入");
        	}
        }
        //验证手机号是否存在
        if (memberDao.mobileIsExist(member.getMobile()) == true) {
            throw new MemberExistingException("手机号已存在");
        }
        //手机已绑定
        member.setMobileIsBind(State.YES);
        //会员名
        String memberName = "";
        boolean memberNameAble = false;
        for(int i = 0; i<100; i++) {
            if (memberNameAble == true) {
                break;
            }
            memberName = memberDao.createMemberName();
            Member memberTmp = memberDao.getMemberInfoByMemberName(memberName);
            if (memberTmp == null) {
                memberNameAble = true;
            }else{
                memberName = "";
            }
        }
        if (memberName.length() <= 0) {
            throw new ShopException("注册失败");
        }
        member.setMemberName(memberName);
        //密码
        member.setMemberPwd(ShopHelper.getMd5(member.getMemberPwd()));
        Timestamp currTime = new Timestamp(System.currentTimeMillis());
        //注册时间
        member.setRegisterTime(currTime);
        //登录时间
        member.setLoginTime(currTime);
        //登录次数
        member.setLoginNum(1);
        //性别
        member.setMemberSex(SexType.SECRECY);
        //状态
        member.setState(MemberState.OPEN);
        //购买权限
        member.setAllowBuy(State.YES);
        //发言权限
        member.setAllowTalk(State.YES);
        //IP
        member.setLoginIp(ShopHelper.getAddressIP());
        Serializable memberId = memberDao.save(member);
        //会员ID
        member.setMemberId((Integer) memberId);
        //增加积分
        pointsService.addPointsRegister(member.getMemberId());
        //增加经验
        experienceService.addExperienceRegister(member.getMemberId());
        return member;
    }
    /**
     * 会员普通登录
     * @param loginName
     * @param memberPwd
     * @return
     * @throws ShopException
     */
    public Member login(String loginName, String memberPwd) throws ShopException{
        if (loginName==null || loginName.equals("")) {
            throw new ParameterErrorException();
        }
        HashMap<String,String> where = new HashMap<String,String>();
        where.put("state", "state = :state");
        where.put("memberPwd", "memberPwd = :memberPwd");
        HashMap<String,Object> params = new HashMap<String,Object>();
        params.put("state", MemberState.OPEN);
        params.put("memberPwd", ShopHelper.getMd5(memberPwd));
        String loginType = "";
        //手机号码登录
        Pattern patternMobile = Pattern.compile("^[1][0-9]{10}$");
        Matcher matcherMobile = patternMobile.matcher(loginName);
        if (matcherMobile.matches()) {
            loginType = "mobile";
        }
        //邮箱登录
        Pattern patternEmail = Pattern.compile(".*@.*");
        Matcher matcherEmail = patternEmail.matcher(loginName);
        if (matcherEmail.matches()) {
            loginType = "email";
        }
        if (loginType.equals("mobile")) {
            where.put("mobile", "mobile = :mobile");
            where.put("mobileIsBind", "mobileIsBind = :mobileIsBind");
            params.put("mobile", loginName);
            params.put("mobileIsBind", State.YES);
        }else if (loginType.equals("email")) {
            where.put("email", "email = :email");
            where.put("emailIsBind", "emailIsBind = :emailIsBind");
            params.put("email", loginName);
            params.put("emailIsBind", State.YES);
        } else {
            where.put("memberName", "memberName = :memberName");
            params.put("memberName", loginName);
        }
        Member member = memberDao.getMemberInfo(where, params);
        if (member == null) {
            throw new ShopException("用户信息错误或者账号已被关闭");
        }
        //更新会员信息
        HashMap<String, Object> updateMap = new HashMap<String, Object>();
        updateMap.put("loginNum", member.getLoginNum()+1);
        updateMap.put("loginTime", ShopHelper.getCurrentTimestamp());
        updateMap.put("lastLoginTime", member.getLoginTime());
        updateMap.put("loginIp", ShopHelper.getAddressIP());
        updateMap.put("lastLoginIp", member.getLoginIp());
        memberDao.updateByMemberId(updateMap, member.getMemberId());
        //增加积分
        pointsService.addPointsLogin(member.getMemberId());
        //增加经验
        experienceService.addExperienceLogin(member.getMemberId());
        return member;
    }
    /**
     * 会员手机动态码登录
     * @param mobile
     * @param authCode
     * @return
     * @throws ShopException
     */
    public Member loginMobile(String mobile, String authCode) throws ShopException {
        if (!siteService.getSiteInfo().get(SiteTitle.SMSLOGIN).equals("1")) {
            throw new ShopException("系统未开启手机登录功能");
        }
        if (mobile==null || mobile.equals("")) {
            throw new ParameterErrorException();
        }
        //验证动态码
        SmsCode smsCode = new SmsCode();
        smsCode.setSendType(SmsCodeSendType.LOGIN);
        smsCode.setMobilePhone(mobile);
        smsCode.setAuthCode(authCode);
        if (smsCodeDao.checkCode(smsCode).equals(false)) {
            throw new ShopException("动态码错误或已过期，重新输入");
        }
        //验证手机号是否存在
        HashMap<String,String> where = new HashMap<String,String>();
        where.put("mobile", "mobile = :mobile");
        where.put("state", "state = :state");
        HashMap<String,Object> params = new HashMap<String,Object>();
        params.put("mobile", mobile);
        params.put("state", MemberState.OPEN);
        Member member = memberDao.getMemberInfo(where,params);
        if (member == null) {
            throw new ShopException("手机号未注册或者账号已被关闭");
        }
        //更新会员信息
        HashMap<String, Object> updateMap = new HashMap<String, Object>();
        updateMap.put("loginNum", member.getLoginNum()+1);
        updateMap.put("loginTime", ShopHelper.getCurrentTimestamp());
        updateMap.put("lastLoginTime", member.getLoginTime());
        updateMap.put("loginIp", ShopHelper.getAddressIP());
        updateMap.put("lastLoginIp", member.getLoginIp());
        memberDao.updateByMemberId(updateMap, member.getMemberId());
        //增加积分
        pointsService.addPointsLogin(member.getMemberId());
        //增加经验
        experienceService.addExperienceLogin(member.getMemberId());
        return member;
    }
    /**
     * 添加用户
     * @param member
     * @return
     * @throws MemberExistingException
     */
    public Serializable add(Member member) throws MemberExistingException {
        //验证用户名是否存在
        if (memberDao.memberNameIsExist(member.getMemberName()) == true) {
            throw new MemberExistingException("用户名已存在");
        }
        //验证邮箱是否存在
        if (!(member.getEmail()==null || member.getEmail().equals("")) && memberDao.emailIsExist(member.getEmail()) == true) {
            throw new MemberExistingException("邮箱已存在");
        }
        //验证手机号是否存在
        if (!(member.getMobile()==null || member.getMobile().equals("")) && memberDao.mobileIsExist(member.getMobile()) == true) {
            throw new MemberExistingException("手机号已存在");
        }
        //密码
        member.setMemberPwd(ShopHelper.getMd5(member.getMemberPwd()));
        Timestamp currTime = new Timestamp(System.currentTimeMillis());
        //注册时间
        member.setRegisterTime(currTime);
        //性别
        member.setMemberSex(SexType.SECRECY);
        //状态
        member.setState(MemberState.OPEN);
        //购买权限
        member.setAllowBuy(State.YES);
        //发言权限
        member.setAllowTalk(State.YES);
        return memberDao.save(member);
    }
    /**
     * 绑定邮箱
     * @param authCode
     * @param email
     * @param memberId
     * @return
     * @throws ShopException
     */
    public boolean bindingEmail(String authCode, String email, int memberId) throws ShopException {
        //验证邮箱是否存在
        if (memberDao.emailIsExist(email) == true) {
            throw new MemberExistingException("邮箱已存在，请使用其他邮箱");
        }
        //验证动态码
        EmailCode emailCode = new EmailCode();
        emailCode.setSendType(EmailCodeSendType.EMAILBIND);
        emailCode.setEmail(email);
        emailCode.setAuthCode(authCode);
        if (emailCodeDao.checkCode(emailCode).equals(false)) {
            throw new ShopException("动态码错误或已过期，重新输入");
        } else {
            //更新会员
            HashMap<String, Object> updateMap = new HashMap<String, Object>();
            updateMap.put("email", email);
            updateMap.put("emailIsBind", State.YES);
            memberDao.updateByMemberId(updateMap, memberId);
            return true;
        }
    }
    /**
     * 绑定手机
     * @param authCode
     * @param mobile
     * @param memberId
     * @return
     * @throws ShopException
     */
    public boolean bindingMobile(String authCode, String mobile, int memberId) throws ShopException {
        //验证手机号是否存在
        if (memberDao.mobileIsExist(mobile) == true) {
            throw new MemberExistingException("手机号已存在，请使用其他手机号");
        }
        //验证动态码
        SmsCode smsCode = new SmsCode();
        smsCode.setSendType(SmsCodeSendType.MOBILEBIND);
        smsCode.setMobilePhone(mobile);
        smsCode.setAuthCode(authCode);
        if (smsCodeDao.checkCode(smsCode).equals(false)) {
            throw new ShopException("动态码错误或已过期，重新输入");
        } else {
            //更新会员
            HashMap<String, Object> updateMap = new HashMap<String, Object>();
            updateMap.put("mobile", mobile);
            updateMap.put("mobileIsBind", State.YES);
            memberDao.updateByMemberId(updateMap, memberId);
            return true;
        }
    }
    /**
     * 修改密码
     * @param memberPwd
     * @param repeatMemberPwd
     * @param memberId
     * @return
     * @throws ShopException
     */
    public boolean modifyMemberPwd(String memberPwd, String repeatMemberPwd, int memberId) throws ShopException {
        if (!repeatMemberPwd.equals(memberPwd)) {
            throw new ShopException("两次输入的密码不一致");
        }
        if (memberPwd.length() < 6 || memberPwd.length() > 20){
            throw new ShopException("密码长度应在6-20个字符之间");
        }
        //更新会员
        HashMap<String, Object> updateMap = new HashMap<String, Object>();
        updateMap.put("memberPwd", ShopHelper.getMd5(memberPwd));
        memberDao.updateByMemberId(updateMap, memberId);
        return true;
    }
    /**
     * 修改支付密码
     * @param payPwd
     * @param repeatPayPwd
     * @param memberId
     * @return
     * @throws ShopException
     */
    public boolean modifyPayPwd(String payPwd, String repeatPayPwd, int memberId) throws ShopException {
        if (!repeatPayPwd.equals(payPwd)) {
            throw new ShopException("两次输入的密码不一致");
        }
        if (payPwd.length() < 6 || payPwd.length() > 20){
            throw new ShopException("密码长度应在6-20个字符之间");
        }
        //更新会员
        HashMap<String, Object> updateMap = new HashMap<String, Object>();
        updateMap.put("payPwd", ShopHelper.getMd5(payPwd));
        memberDao.updateByMemberId(updateMap, memberId);
        return true;
    }
    /**
     * 修改会员预存款
     * @param predepositAvailable
     * @param predepositFreeze
     * @param memberId
     * @return
     * @throws ShopException
     */
    public boolean modifyPredeposit(BigDecimal predepositAvailable, BigDecimal predepositFreeze, int memberId) throws ShopException {
        if (memberId <= 0) {
            throw new ShopException("会员信息错误");
        }
        Member member = memberDao.get(Member.class, memberId);
        if (member == null) {
            throw new ShopException("会员信息错误");
        }
        HashMap<String,Object> updateMap = new HashMap<String, Object>();
        if (predepositAvailable != null) {
            updateMap.put("predepositAvailable", PriceHelper.add(member.getPredepositAvailable(), predepositAvailable));
        }
        if (predepositFreeze != null) {
            updateMap.put("predepositFreeze", PriceHelper.add(member.getPredepositFreeze(), predepositFreeze));
        }
        memberDao.updateByMemberId(updateMap, memberId);
        return true;
    }
    /**
     * 获取会员列表表格数据
     * @param dtGridPager
     * @return
     * @throws Exception
     */
    public DtGrid getMemberDtGridList(String dtGridPager) throws Exception {
        return memberDao.getDtGridList(dtGridPager, Member.class);
    }
    /**
     * 编辑会员信息
     * @param member
     * @return
     * @throws MemberExistingException
     */
    public Boolean update(Member member) throws MemberExistingException {
        //设置列默认值
        if (new SexType().isExistValue(member.getMemberSex()) == false) {
            member.setMemberSex(SexType.SECRECY);
        }
        State state = new State();
        if (state.isExistValue(member.getAllowBuy()) == false) {
            member.setAllowBuy(State.NO);
        }
        if (state.isExistValue(member.getAllowTalk()) == false) {
            member.setAllowTalk(State.NO);
        }
        if (new MemberState().isExistValue(member.getState()) == false) {
            member.setState(MemberState.CLOSE);
        }
        memberDao.update(member);
        return true;
    }
    /**
     * 更新会员令牌记录
     * @param memberToken
     * @return
     */
    public String updateMemberToken(MemberToken memberToken) {
        //删除已有token
        memberTokenDao.deleteMemberTokenByMemberId(memberToken.getMemberId(), memberToken.getClientType());
        //新增token
        memberToken.setAddTime(ShopHelper.getCurrentTimestamp());
        for(int i=0; i<101; i=i+1) {
            //如果已经循环100从则直接终止
            if (i == 100) {
                return "";
            }
            String token = memberTokenDao.getTokenCode();
            MemberToken memberToken1 = memberTokenDao.getMemberTokenByToken(token);
            if (memberToken1 == null) {
                break;
            }
        }
        memberToken.setToken(memberTokenDao.getTokenCode());
        memberTokenDao.save(memberToken);
        return memberToken.getToken();
    }
    /**
     * 通过会员令牌查询会员信息
     * @param token
     * @return
     */
    public Member getMemberInfoByToken(String token){
        MemberToken memberToken = memberTokenDao.getMemberTokenByToken(token);
        Member member = null;
        if (memberToken == null) {
            return member;
        }
        member = memberDao.get(Member.class, memberToken.getMemberId());
        return member;
    }
    /**
     * 验证支付密码
     * @param payPwd
     * @param memberId
     * @throws ShopException
     */
    public void validatePayPwd(String payPwd,int memberId) throws ShopException {
        if (payPwd.length() < 6 || payPwd.length() > 20){
            throw new ShopException("密码长度应在6-20个字符之间");
        }
        Member member = memberDao.get(Member.class,memberId);
        if (member == null) {
            throw new ShopException("该会员不存在");
        }
        if (member.getPayPwd() == null || member.getPayPwd().equals("")) {
            throw new ShopException("您还未设置支付密码，请设置支付密码");
        }
        if (member.getPayPwd().equals(ShopHelper.getMd5(payPwd)) == false) {
            throw new ShopException("支付密码输入不正确");
        }
//        if (PriceHelper.isGreaterThan(member.getPredepositAvailable(),new BigDecimal(0)) == false) {
//            throw new ShopException("");
//        }
    }
    
    /**
     * 发送卡券
     */
    public String sendCardCoupons(int memberId, String memberName, String returnAmount, String startTime, String endTime, int goodsId, int ordersId, int storeId){
    	String message = "";
    	HashMap<String, Object> map = new HashMap<String ,Object>();
        //请求K码所需参数
        map.put("systemAp","ec104");
        map.put("userId", memberId);
        map.put("userName", memberName);
        map.put("startMoney",returnAmount);
        map.put("endMoney", returnAmount);
		if(!UtilsHelper.isEmpty(startTime)){
			map.put("startTime", startTime);
		}
		if(!UtilsHelper.isEmpty(endTime)){
			map.put("endTime", endTime);
		}
		try {
			//获取K码
			JSONObject json = KmsHelper.sendToKms(map, "distrElecKCodeNoSms");
			JSONObject data =  json.getJSONObject("data");
			if(null != data){
				//新增一条卡券信息
		        Coupons coupon = new Coupons();
		        coupon.setMemberId(memberId);
		        coupon.setOrdersId(ordersId);
		        coupon.setCodeKey(data.get("cardNo").toString());
		        coupon.setkCode(data.get("keyCode").toString());
		        coupon.setGoodsId(goodsId);
		        coupon.setStoreId(storeId);
		        coupon.setCreateTime(Timestamp.valueOf(data.get("startTime").toString()+ " 00:00:00"));
		        coupon.setDueTime(Timestamp.valueOf(data.get("endTime").toString()+ " 00:00:00"));
		        coupon.setAmountMoney(new BigDecimal(data.get("money").toString()));
		        coupon.setIsUseful(1);
				this.addCoupons(coupon);
			}else{
				message = json.get("message").toString();
			}
		} catch (IOException | ShopException e) {
			message = "K码获取错误";
			e.printStackTrace();
		}
		return message;
    }
    
    /**
     * 用户添加卡券
     * @param member
     * @return
     * @throws ShopException
     */
    public int addCoupons(Coupons coupons) throws ShopException {
        Serializable couponsId = couponsDao.save(coupons);
        return (Integer)couponsId;
    }
    /**
     * 根据卡券id查询卡券信息
     * @param member
     * @return
     * @throws ShopException
     */
    public Coupons findCoupons(int couponsId) throws ShopException {
    	Coupons coupons = couponsDao.get(Coupons.class,couponsId);
        return coupons;
    }
    /**
     * 根据用户ID和订单ID查询卡券信息
     * @param member
     * @return
     * @throws ShopException
     */
    public List<Coupons> getCoupon(HashMap<String, Integer> param) throws ShopException {
    	List<Coupons> coupons = couponsDao.getCoupon(param);
        return coupons;
    }
    /**
     * 获取用户卡券列表
     * @param member
     * @return
     * @throws ShopException
     */
    public List<Coupons> getCouponsList(int memberId, String memberName) throws ShopException {
    	List<Coupons> coupons = couponsDao.getCouponsList(memberId);
    	for(Coupons c : coupons){
    		String cardNo = c.getCodeKey();
			HashMap<String, Object> map = new HashMap<String ,Object>();
	        //请求K码所需参数
	        map.put("systemAp","ec104");
	        map.put("userId", memberId);
	        map.put("userName", memberName);
	        map.put("cardNo",cardNo);
	        try {
	        	JSONObject json = KmsHelper.sendToKms(map, "getInfoByCardNo");
				JSONObject data =  json.getJSONObject("data");
				if(null != data){
					//更新卡券的状态
					int status = Integer.parseInt(UtilsHelper.getString(data.get("status")));
					if (status != c.getIsUseful()){
						c.setIsUseful(status);
						this.updateCoupons(c);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("请求卡号：" +  cardNo + " 的状态异常。");
			}
		}
        return coupons;
    }
    /**
     * 卡券失效
     */
    public void updateCoupons(Coupons coupons) {
        couponsDao.update(coupons);
    }
    
}