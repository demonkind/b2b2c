package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.SiteTitle;
import net.shopnc.b2b2c.dao.member.ConsultClassDao;
import net.shopnc.b2b2c.domain.member.ConsultClass;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.SiteService;
import net.shopnc.b2b2c.service.member.ConsultService;
import net.shopnc.common.entity.ResultEntity;
import net.shopnc.common.entity.dtgrid.DtGrid;
import net.shopnc.common.util.SecurityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * Created by zxy on 2016-03-14
 */
@Controller
public class ConsultJsonAction extends BaseJsonAction {
    @Autowired
    private SiteService siteService;
    @Autowired
    private ConsultService consultService;
    @Autowired
    private ConsultClassDao consultClassDao;
    @Autowired
    SecurityHelper securityHelper;

    /**
     * 保存咨询设置
     */
    @ResponseBody
    @RequestMapping(value = "consult/setting", method = RequestMethod.POST)
    public ResultEntity setting(@RequestParam("consultPrompt") String consultPrompt,
                                @RequestParam("guestConsult") String guestConsult) {
        HashMap<String,String> siteList = new HashMap<String, String>();
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setUrl(ShopConfig.getAdminRoot() + "consult/setting");
        try {
            siteList.put(SiteTitle.CONSULTPROMPT, securityHelper.xssClean(consultPrompt));
            if (guestConsult.equals("1")) {
                siteList.put(SiteTitle.GUESTCONSULT, "1");
            }else{
                siteList.put(SiteTitle.GUESTCONSULT, "0");
            }
            siteService.updateSite(siteList);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("操作成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("数据库保存失败");
        }
        return resultEntity;
    }
    /**
     * 类型列表JSON数据
     */
    @ResponseBody
    @RequestMapping(value = "consult/class.json", method = RequestMethod.POST)
    public DtGrid classListJson(String dtGridPager) {
        DtGrid dtGrid = new DtGrid();
        try {
            dtGrid = consultService.getConsultClassDtGridList(dtGridPager);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            dtGrid.setIsSuccess(false);
        }
        return dtGrid;
    }
    /**
     * 新增咨询类型
     */
    @ResponseBody
    @RequestMapping(value = "consult/class/add", method = RequestMethod.POST)
    public ResultEntity classAdd(ConsultClass consultClass) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            //保存类型
            consultClass.setIntroduce(securityHelper.xssClean(consultClass.getIntroduce()));
            consultClassDao.save(consultClass);

            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("新增成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("新增失败");
        }
        return resultEntity;
    }
    /**
     * 编辑咨询类型
     */
    @ResponseBody
    @RequestMapping(value = "consult/class/edit", method = RequestMethod.POST)
    public ResultEntity classEdit(ConsultClass consultClass) {
        ResultEntity resultEntity = new ResultEntity();

        ConsultClass updateClass = consultClassDao.get(ConsultClass.class, consultClass.getClassId());
        updateClass.setClassName(consultClass.getClassName());
        updateClass.setClassSort(consultClass.getClassSort());
        updateClass.setIntroduce(securityHelper.xssClean(consultClass.getIntroduce()));
        try {
            //保存类型
            consultClassDao.update(updateClass);

            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("编辑成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("编辑失败");
        }
        return resultEntity;
    }
    /**
     * 删除咨询类型
     */
    @ResponseBody
    @RequestMapping(value = "consult/class/del", method = RequestMethod.POST)
    public ResultEntity classEdit(Integer classId) {
        ResultEntity resultEntity = new ResultEntity();
        if (classId == null || classId <= 0) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("参数错误");
            return resultEntity;
        }
        try {
            //删除类型
            boolean r = consultService.delConsultClass(classId);
            if (r == true) {
                resultEntity.setCode(ResultEntity.SUCCESS);
                resultEntity.setMessage("删除成功");
                return resultEntity;
            }else{
                resultEntity.setCode(ResultEntity.FAIL);
                resultEntity.setMessage("删除失败");
                return resultEntity;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
            return resultEntity;
        }
    }
    /**
     * 咨询列表JSON数据
     */
    @ResponseBody
    @RequestMapping(value = "consult/list.json", method = RequestMethod.POST)
    public DtGrid listJson(String dtGridPager) {
        DtGrid dtGrid = new DtGrid();
        try {
            dtGrid = consultService.getConsultDtGridList(dtGridPager);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            dtGrid.setIsSuccess(false);
        }
        return dtGrid;
    }
    /**
     * 删除咨询
     */
    @ResponseBody
    @RequestMapping(value = "consult/del", method = RequestMethod.POST)
    public ResultEntity del(Integer consultId) {
        ResultEntity resultEntity = new ResultEntity();
        if (consultId==null || consultId<=0) {
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage("参数错误");
            return resultEntity;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("consultId", consultId);
        try {
            consultService.deleteConsult(params);
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("删除成功");
            return resultEntity;
        } catch (ShopException e) {
            logger.warn(e.getMessage());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
            return resultEntity;
        }
    }
}
