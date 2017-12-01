package net.shopnc.b2b2c.web.action.home;

import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.member.GoodsBrowseService;
import net.shopnc.b2b2c.vo.goodsbrowse.GoodsBrowseVo;
import net.shopnc.b2b2c.web.common.entity.SessionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxy on 2016-01-28
 */
@Controller
public class HomeGoodsBrowseAction extends HomeBaseAction {

    @Autowired
    private GoodsBrowseService goodsBrowseService;

    /**
     * 最近浏览商品
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "goods/browse/list", method = RequestMethod.GET)
    public String consultList(ModelMap modelMap) {
        List<GoodsBrowseVo> browseList = new ArrayList<>();
        try{
            browseList = goodsBrowseService.getGoodsBrowseList(SessionEntity.getMemberId(), 20);
        }catch (ShopException e){
            logger.info(e.getMessage());
        }
        //查询咨询列表
        modelMap.put("browseList", browseList);
        return getHomeTemplate("search/goods_browse");
    }
}