package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.dao.member.MemberDao;
import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.exception.MemberExistingException;
import net.shopnc.b2b2c.service.member.MemberService;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.entity.dtgrid.DtGrid;
import net.shopnc.common.util.ShopHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.io.Serializable;

/**
 * Created by zxy on 2016-03-14.
 */
@Controller
public class MemberJsonAction extends BaseJsonAction {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberDao memberDao;

    /**
     * 会员列表JSON数据
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "member/list.json", method = RequestMethod.POST)
    public DtGrid listJson(String dtGridPager) {
        DtGrid dtGrid = new DtGrid();
        try {
            dtGrid = memberService.getMemberDtGridList(dtGridPager);
        } catch (Exception e) {
            logger.error(e.getMessage());
            dtGrid.setIsSuccess(false);
        }
        return dtGrid;
    }
    /**
     * 编辑会员
     * @param member
     * @param bindingResult
     * @param repeatMemberPwd
     * @param provinceid
     * @param cityid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "member/edit", method = RequestMethod.POST)
    public ResultEntity edit(@Valid Member member,
                             BindingResult bindingResult,
                             @RequestParam("repeatMemberPwd") String repeatMemberPwd,
                             @RequestParam(value = "memberAddress_1", required = false) int provinceid,
                             @RequestParam(value = "memberAddress_2", required = false) int cityid) {

        ResultEntity resultEntity = new ResultEntity();
        //数据验证
        if (!repeatMemberPwd.equals(member.getMemberPwd())) {
            logger.info("两次输入的密码不一致");
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("两次输入的密码不一致");
            return resultEntity;
        }
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.info(error.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("编辑失败");
            return resultEntity;
        }
        //查询会员信息
        Member memberUpdate = memberDao.get(Member.class, member.getMemberId());
        if (memberUpdate == null) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("会员信息错误");
            return resultEntity;
        }
        //邮箱
        if ((member.getEmail()!=null && member.getEmail()!="") && memberDao.emailIsExist(member.getEmail(), member.getMemberId()) == true) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("邮箱已存在");
            return resultEntity;
        }
        memberUpdate.setEmail(member.getEmail());
        //手机号
        if ((member.getMobile()!=null && member.getMobile()!="") && memberDao.mobileIsExist(member.getMobile(), member.getMemberId()) == true) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("手机号已存在");
            return resultEntity;
        }
        memberUpdate.setMobile(member.getMobile());
        //密码
        if (member.getMemberPwd()!=null && member.getMemberPwd()!="") {
            memberUpdate.setMemberPwd(ShopHelper.getMd5(member.getMemberPwd()));
        }
        //真实姓名
        memberUpdate.setTrueName(member.getTrueName());
        //头像
        memberUpdate.setAvatar(member.getAvatar());
        //性别
        memberUpdate.setMemberSex(member.getMemberSex());
        //出生日期
        memberUpdate.setBirthday(member.getBirthday());
        //所在地
        memberUpdate.setAddressProvinceId(provinceid);
        memberUpdate.setAddressCityId(cityid);
        memberUpdate.setAddressAreaId(member.getAddressAreaId());
        memberUpdate.setAddressAreaInfo(member.getAddressAreaInfo());
        //QQ
        memberUpdate.setMemberQQ(member.getMemberQQ());
        //旺旺
        memberUpdate.setMemberWW(member.getMemberWW());
        //允许购买商品
        memberUpdate.setAllowBuy(member.getAllowBuy());
        //允许发表言论
        memberUpdate.setAllowTalk(member.getAllowTalk());
        //会员状态
        memberUpdate.setState(member.getState());
        try {
            //注册用户
            memberService.update(memberUpdate);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("编辑成功");
            return resultEntity;
        } catch (MemberExistingException e) {
            logger.warn(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
            return resultEntity;
        }
    }
    /**
     * 新增会员
     * @param member
     * @param bindingResult
     * @param repeatMemberPwd
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "member/add", method = RequestMethod.POST)
    public ResultEntity add(@Valid Member member,
                       BindingResult bindingResult,
                       @RequestParam("repeatMemberPwd") String repeatMemberPwd){

        ResultEntity resultEntity = new ResultEntity();
        //数据验证
        if (!repeatMemberPwd.equals(member.getMemberPwd())) {
            logger.info("两次输入的密码不一致");
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("两次输入的密码不一致");
            return resultEntity;
        }
        if (bindingResult.hasErrors()) {
            //获取所有错误信息
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.info(error.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("新增失败");
            return resultEntity;
        }
        try {
            //注册用户
            Serializable memberId = memberService.add(member);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
            return resultEntity;
        } catch (MemberExistingException e) {
            logger.warn(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
            return resultEntity;
        }
    }
    /**
     * 验证会员名是否存在
     * @param memberName
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "member/membernameexist", method = RequestMethod.GET)
    public ResultEntity memberNameExist(String memberName){
        ResultEntity resultEntity = new ResultEntity();
        if (memberDao.memberNameIsExist(memberName) == false) {
            resultEntity.setCode(ResultEntity.SUCCESS);
        } else {
            resultEntity.setCode(ResultEntity.FAIL);
        }
        return resultEntity;
    }
    /**
     * 验证会员邮箱是否存在
     * @param email
     * @param memberId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "member/emailexist", method = RequestMethod.GET)
    public ResultEntity emailExist(String email,int memberId){
        ResultEntity resultEntity = new ResultEntity();
        boolean result;
        if (memberId > 0) {
            result = memberDao.emailIsExist(email,memberId);
        }else{
            result = memberDao.emailIsExist(email);
        }
        if (result == false) {
            resultEntity.setCode(ResultEntity.SUCCESS);
        } else {
            resultEntity.setCode(ResultEntity.FAIL);
        }
        return resultEntity;
    }
    /**
     * 验证会员手机是否存在
     * @param mobile
     * @param memberId
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "member/mobileexist", method = RequestMethod.GET)
    public ResultEntity mobileExist(String mobile,int memberId) throws Exception {
        ResultEntity resultEntity = new ResultEntity();
        boolean result;
        if (memberId > 0) {
            result = memberDao.mobileIsExist(mobile, memberId);
        }else{
            result = memberDao.mobileIsExist(mobile);
        }
        if (result == false) {
            resultEntity.setCode(ResultEntity.SUCCESS);
        } else {
            resultEntity.setCode(ResultEntity.FAIL);
        }
        return resultEntity;
    }
}
