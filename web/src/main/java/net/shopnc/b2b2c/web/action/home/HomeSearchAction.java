package net.shopnc.b2b2c.web.action.home;

import net.shopnc.b2b2c.constant.BrandShowType;
import net.shopnc.b2b2c.service.SearchService;
import net.shopnc.b2b2c.vo.goods.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品列表页
 * Created by shopnc.feng on 2015-12-10.
 */
@Controller
public class HomeSearchAction extends HomeBaseAction {
    @Autowired
    SearchService searchService;

    /**
     * 商品搜索列表
     * @param page
     * @param categoryId
     * @param brandId
     * @param attr
     * @param sort
     * @param express
     * @param own
     * @param keyword
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "search", method = RequestMethod.GET)
    public String search(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                         @RequestParam(name = "cat", required = false, defaultValue = "0") Integer categoryId,
                         @RequestParam(name = "brand", required = false, defaultValue = "0") Integer brandId,
                         @RequestParam(name = "attr", required = false, defaultValue = "") String attr,
                         @RequestParam(name = "sort", required = false, defaultValue = "") String sort,
                         @RequestParam(name = "express", required = false, defaultValue = "0") Integer express,
                         @RequestParam(name = "own", required = false, defaultValue = "0") Integer own,
                         @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
                         @RequestParam(name = "activityId", required = false, defaultValue = "") String activityId,
                         ModelMap modelMap) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        modelMap.put("paramMap", request.getParameterMap());
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("page", page);
        params.put("categoryId", categoryId);
        params.put("brandId", brandId);
        params.put("attr", attr);
        params.put("sort", sort);
        params.put("express", express);
        params.put("own", own);
        params.put("keyword", keyword);
        params.put("activityId", activityId);
        modelMap.put("brandShowType", new BrandShowType().get());

        try {
            Map<String, Object> map = searchService.search(params);
            modelMap.addAllAttributes(map);


            // 搜索页顶部热销商品，暂时从查询结果中随机取4条
            List<GoodsVo> goodsList = (List<GoodsVo>) map.get("list");
            int toIndex = 4 < goodsList.size() ? 4 : goodsList.size();
            if (toIndex > 0) {
                goodsList = goodsList.subList(0, toIndex);
            }
            modelMap.put("topHotGoodsList", goodsList);
            modelMap.put("keyword", keyword);


        } catch (Exception e) {
            logger.error(e.toString());
        }
        return getHomeTemplate("search");
    }
}
