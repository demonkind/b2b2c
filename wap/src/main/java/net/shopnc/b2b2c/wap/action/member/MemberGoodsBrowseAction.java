package net.shopnc.b2b2c.wap.action.member;

import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.service.member.GoodsBrowseService;
import net.shopnc.b2b2c.wap.common.entity.SessionEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

/**
 * Created by zxy on 2016-03-14.
 */

@Controller
public class MemberGoodsBrowseAction extends MemberBaseAction {

    @Autowired
    private GoodsBrowseService goodsBrowseService;

    /**
     * 商品浏览历史列表
     * @param goodsCategoryId1
     * @param goodsCategoryId2
     * @param page
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "goodsbrowse", method = RequestMethod.GET)
    public String index(@RequestParam(value = "gc_id1", required = false) Integer goodsCategoryId1,
                        @RequestParam(value = "gc_id2", required = false) Integer goodsCategoryId2,
                        @RequestParam(value = "page", required = false, defaultValue="1") Integer page,
                        ModelMap modelMap) {
        //删除30天前的浏览历史
        try {
            goodsBrowseService.delGoodsBrowseExpire(SessionEntity.getMemberId());
        }catch (ShopException e){
            logger.info(e.getMessage());
        }
        //查询浏览商品分类
        HashMap<String,Object> browseCategoryList = goodsBrowseService.getGoodsBrowseCategoryList(SessionEntity.getMemberId());
        modelMap.put("browseCategoryList", browseCategoryList);
        //查询浏览历史
        HashMap<String,Object> result = goodsBrowseService.getGoodsBrowseTimelineList(SessionEntity.getMemberId(), goodsCategoryId1, goodsCategoryId2, page, 0);
        modelMap.put("browseList", result.get("list"));
        modelMap.put("showPage", result.get("showPage"));
        //menuKey
        modelMap.put("menuKey", "goodsbrowse");
        return getMemberTemplate("goods_browse");
    }
}