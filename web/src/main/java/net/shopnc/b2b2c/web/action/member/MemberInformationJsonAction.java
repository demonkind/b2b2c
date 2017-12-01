package net.shopnc.b2b2c.web.action.member;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.dao.member.MemberDao;
import net.shopnc.b2b2c.domain.member.Member;
import net.shopnc.b2b2c.exception.MemberExistingException;
import net.shopnc.b2b2c.service.UploadService;
import net.shopnc.b2b2c.service.member.MemberService;
import net.shopnc.b2b2c.web.common.entity.SessionEntity;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.util.ShopHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.util.HashMap;

/**
 * Created by zxy on 2016-03-14.
 */

@Controller
public class MemberInformationJsonAction extends MemberBaseJsonAction {

    @Autowired
    MemberService memberService;
    @Autowired
    UploadService uploadService;
    @Autowired
    MemberDao memberDao;

    /**
     * 保存会员基本信息
     * @param member
     * @param bindingResult
     * @param provinceid
     * @param cityid
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "information", method = RequestMethod.POST)
    public ResultEntity save(@Valid Member member,
                       BindingResult bindingResult,
                       @RequestParam(value = "memberAddress_1", required = false) int provinceid,
                       @RequestParam(value = "memberAddress_2", required = false) int cityid) {
        ResultEntity resultEntity = new ResultEntity();
        if (member.getAddressAreaId() > 0) {
            if (provinceid <= 0 || cityid <= 0) {
                resultEntity.setCode(ResultEntity.FAIL);
                resultEntity.setMessage("所在地区错误");
                return resultEntity;
            }
        }
        if (bindingResult.hasErrors()) {
            //获取所有错误信息
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.info(error.getDefaultMessage());
            }
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("编辑失败");
            return resultEntity;
        }
        //查询会员信息
        Member memberUpdate = memberDao.get(Member.class,SessionEntity.getMemberId());
        //修改用户名
        if (memberUpdate.getModifyNum()==0 && member.getMemberName()!=null && !member.getMemberName().equals(memberUpdate.getMemberName())) {
            //验证用户名是否存在
            if (memberDao.memberNameIsExist(member.getMemberName(),memberUpdate.getMemberId()) == true) {
                resultEntity.setCode(ResultEntity.FAIL);
                resultEntity.setMessage("用户名已存在");
                return resultEntity;
            }
            memberUpdate.setMemberName(member.getMemberName());
            memberUpdate.setModifyNum(1);
        }
        //真实姓名
        memberUpdate.setTrueName(member.getTrueName());
        //性别
        memberUpdate.setMemberSex(member.getMemberSex());
        //生日
        memberUpdate.setBirthday(member.getBirthday());
        //所在地区
        memberUpdate.setAddressProvinceId(provinceid);
        memberUpdate.setAddressCityId(cityid);
        memberUpdate.setAddressAreaId(member.getAddressAreaId());
        memberUpdate.setAddressAreaInfo(member.getAddressAreaInfo());
        //QQ
        memberUpdate.setMemberQQ(member.getMemberQQ());
        //旺旺
        memberUpdate.setMemberWW(member.getMemberWW());
        try {
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
     * 验证会员名是否存在
     * @param memberName
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "information/membernameexist", method = RequestMethod.GET)
    public boolean memberNameExist(String memberName){
        if (memberDao.memberNameIsExist(memberName, SessionEntity.getMemberId()) == false) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 上传保存会员头像
     * @param avatar
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "information/avatar", method = RequestMethod.POST)
    public ResultEntity uploadAvatarSave(@RequestParam(value = "avatar") CommonsMultipartFile avatar){
        ResultEntity resultEntity = new ResultEntity();
        HashMap<String, String> map;
        try {
            map = uploadService.memberUpload(avatar);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("上传失败");
            return resultEntity;
        }
        //生成缩略图
        try {
            //ShopHelper.imageThumb(map.get("path"), 500, 500);
            //保存头像
            Member memberInfo = memberDao.get(Member.class, SessionEntity.getMemberId());
            //更新会员
            HashMap<String,Object> updateMap = new HashMap<String, Object>();
            updateMap.put("avatar", map.get("name"));
            memberDao.updateByMemberId(updateMap, SessionEntity.getMemberId());
            //删除已有图片
            if (memberInfo.getAvatar()!=null && memberInfo.getAvatar().length()>0) {
                File imgFile = new File(ShopConfig.getUploadPath()+memberInfo.getAvatar());
                if (imgFile.exists()==true && imgFile.isFile()==true) {
                    imgFile.delete();
                }
            }
            //获取图片尺寸
            /*File imgFile = new File(map.get("path"));
            Image img = ImageIO.read(imgFile);
            String width = String.valueOf(img.getWidth(null));
            String height = String.valueOf(img.getHeight(null));
            map.put("imgWidth", width);
            map.put("imgHeight", height);*/
        }catch (Exception e){
            logger.error(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
            return resultEntity;
        }
        resultEntity.setData(map);
        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setMessage("上传成功");
        return resultEntity;
    }
    /**
     * 会员头像裁切
     * @param avatarPath
     * @param avatarName
     * @param width
     * @param height
     * @param x1
     * @param y1
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "information/avatar/cut", method = RequestMethod.POST)
    public ResultEntity uploadAvatarCut(@RequestParam(value = "avatarPath") String avatarPath,
                                        @RequestParam(value = "avatarName") String avatarName,
                                        @RequestParam(value = "width") Integer width,
                                        @RequestParam(value = "height") Integer height,
                                        @RequestParam(value = "x1") Integer x1,
                                        @RequestParam(value = "y1") Integer y1){
        ResultEntity resultEntity = new ResultEntity();
        try{
            ShopHelper.imageCut(avatarPath, width, height, x1, y1);
            //查询会员信息
            Member memberInfo = memberDao.get(Member.class, SessionEntity.getMemberId());
            //更新会员
            HashMap<String,Object> updateMap = new HashMap<String, Object>();
            updateMap.put("avatar", avatarName);
            memberDao.updateByMemberId(updateMap, SessionEntity.getMemberId());
            //删除已有图片
            try{
                if (memberInfo.getAvatar()!=null && memberInfo.getAvatar().length()>0) {
                    File imgFile = new File(ShopConfig.getUploadPath()+memberInfo.getAvatar());
                    if (imgFile.exists()==true && imgFile.isFile()==true) {
                        imgFile.delete();
                    }
                }
            } catch (Exception e){
                logger.error(e.getMessage());
            }
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("上传成功");
            return resultEntity;
        }catch (Exception e){
            logger.error(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("上传失败");
            return resultEntity;
        }
    }
}