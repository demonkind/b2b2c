package net.shopnc.b2b2c.web.action.home;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.dao.goods.GoodsDao;
import net.shopnc.b2b2c.dao.member.ConsultClassDao;
import net.shopnc.b2b2c.dao.store.StoreDao;
import net.shopnc.b2b2c.domain.member.Consult;
import net.shopnc.b2b2c.domain.store.Store;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.EvaluateService;
import net.shopnc.b2b2c.service.goods.GoodsService;
import net.shopnc.b2b2c.service.member.ConsultService;
import net.shopnc.b2b2c.vo.CrumbsVo;
import net.shopnc.b2b2c.vo.goods.GoodsVo;
import net.shopnc.b2b2c.vo.member.EvaluateGoodsVo;
import net.shopnc.b2b2c.vo.member.EvaluateStoreVo;
import net.shopnc.b2b2c.web.common.entity.SessionEntity;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zxy on 2016-01-13
 */
@Controller
public class HomeConsultAction extends HomeBaseAction {
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private ConsultService consultService;
    @Autowired
    private ConsultClassDao consultClassDao;
    @Autowired
    private StoreDao storeDao;
    @Autowired
    private EvaluateService evaluateService;

    /**
     * 商品咨询页面
     * @param goodsId
     * @param classId
     * @param page
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "consult", method = RequestMethod.GET)
    public String info(@RequestParam(value = "gid", required = false) Integer goodsId,
                       @RequestParam(value = "cid", required = false) Integer classId,
                       @RequestParam(value = "page", required = false, defaultValue="1") Integer page,
                       ModelMap modelMap) throws ShopException {
        //获取商品信息
        GoodsVo goodsDetail = null;
        try{
            goodsDetail = (GoodsVo)goodsDao.getGoodsVoByGoodsId(goodsId);
        }catch (Exception e){
            return "redirect:/404";
        }
        modelMap.put("goodsDetail", goodsDetail);
        //查询咨询类型
        modelMap.put("consultClassList", consultClassDao.getConsultClassList(""));
        //查询咨询列表
        HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("goodsId", goodsId);
        if (classId!=null && classId>0) {
            params.put("classId", classId);
        }
        HashMap<String, Object> result = consultService.getConsultListByPage(params, page, 0);
        modelMap.put("consultList", result.get("list"));
        modelMap.put("showPage", result.get("showPage"));
        modelMap.put("isAbleConsult", consultService.isAbleConsult(SessionEntity.getMemberId()));
        modelMap.put("memberName", SessionEntity.getMemberName());
        //店铺及评价信息
        EvaluateGoodsVo evaluateGoodsVo = evaluateService.queryGoodsEvaluate(goodsDetail.getGoodsId());
        modelMap.put("evaluateGoodsVo", evaluateGoodsVo);
        Store storeInfo = storeDao.get(Store.class, goodsDetail.getStoreId());
        modelMap.put("storeInfo", storeInfo);
        EvaluateStoreVo evaluateStoreVo = evaluateService.getEvalStoreClass(goodsDetail.getStoreId());
        modelMap.put("evaluateStoreVo", evaluateStoreVo);
        //面包屑
        List<CrumbsVo> crumbsVoList = goodsService.getGoodsCategoryCrumbs(goodsDetail.getCategoryId());
        Collections.reverse(crumbsVoList);
        CrumbsVo crumbsVo = new CrumbsVo();
        crumbsVo.setName(goodsDetail.getGoodsName());
        crumbsVo.setUrl(ShopConfig.getWebRoot()+"goods/"+goodsDetail.getGoodsId());
        crumbsVoList.add(crumbsVo);
        CrumbsVo crumbsVo1 = new CrumbsVo();
        crumbsVo1.setName("商品咨询");
        crumbsVoList.add(crumbsVo1);
        setCrumbsList(crumbsVoList);
        modelMap.put("crumbsList", super.getCrumbsList());
        return getHomeTemplate("consult");
    }

    /**
     * 商品咨询列表
     * @param goodsId
     * @param classId
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "consult/list", method = RequestMethod.GET)
    public String consultList(@RequestParam(value = "gid", required = false) Integer goodsId,
                       @RequestParam(value = "cid", required = false) Integer classId,
                       ModelMap modelMap) {
        //查询咨询类型
        modelMap.put("consultClassList", consultClassDao.getConsultClassList(""));
        //查询咨询列表
        HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("goodsId", goodsId);
        if (classId!=null && classId>0) {
            params.put("classId", classId);
        }
        HashMap<String, Object> result = consultService.getConsultListByPage(params, 1, 0);
        modelMap.put("consultList", result.get("list"));
        return getHomeTemplate("goods/consult_list");
    }
}
