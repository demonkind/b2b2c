package net.shopnc.b2b2c.admin.action;

import net.shopnc.b2b2c.service.statistical.StatOrdersService;
import net.shopnc.b2b2c.vo.statistical.StatOrdersGoodsVo;
import net.shopnc.b2b2c.vo.statistical.StatOrdersVo;
import net.shopnc.common.util.ChartsHelper;
import net.shopnc.common.util.ShopHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
public class IndexJsonAction extends BaseJsonAction {

    @Autowired
    private StatOrdersService statOrdersService;

    /**
     * 7日内热销前3商品分类销售趋势图
     */
    @ResponseBody
    @RequestMapping(value = "index/stat/goodscategory", method = RequestMethod.GET)
    public HashMap<String, Object> goodsCategorySaleTrend() {
        HashMap<String, Object> chartsMap;
        //查询销量排行前3的商品分类
        int days = 7;
        List<StatOrdersGoodsVo> categoryList = statOrdersService.getOrdersGoodsCategoryByOrdersGoodsPriceSum(3, days);
        HashMap<Integer, List<StatOrdersGoodsVo>> categoryDayList = new HashMap<>();
        //查询该分类每天的销量
        if (categoryList!=null && categoryList.size()>0) {
            for (int i = 0; i < categoryList.size(); i++) {
                categoryDayList.put(i, statOrdersService.getOrdersGoodsSalesDayByCategoryId1(days, categoryList.get(i).getOrdersGoods().getCategoryId1()));
            }
        }
        //构造走势图数据
        HashMap<String, Object> chartsParams = new HashMap<>();
        //走势图Y轴提示商品分类名称
        if (categoryList!=null && categoryList.size()>0) {
            String[] labelsArr = new String[categoryList.size()];
            for (int i = 0; i < categoryList.size(); i++) {
                if (categoryList.get(i).getOrdersGoodsCategoryName1()!=null && categoryList.get(i).getOrdersGoodsCategoryName1().length()>0) {
                    labelsArr[i] = categoryList.get(i).getOrdersGoodsCategoryName1();
                }else{
                    labelsArr[i] = "其他";
                }
            }
            chartsParams.put("labels", labelsArr);
        }else{
            chartsParams.put("labels", new String[]{"暂无"});
        }
        //走势图Y轴数据key
        if (categoryList!=null && categoryList.size()>0) {
            String[] ykeysArr = new String[categoryList.size()];
            for (int i = 0; i < categoryList.size(); i++) {
                ykeysArr[i] = "ykeys"+i;
            }
            chartsParams.put("ykeys", ykeysArr);
        }else{
            chartsParams.put("ykeys", new String[]{"ykeys0"});
        }
        //走势图线条颜色
        String[] lineColorsArrTmp = new String[]{ChartsHelper.colorArr.get("blue"),ChartsHelper.colorArr.get("dark"),ChartsHelper.colorArr.get("orange")};
        if (categoryList!=null && categoryList.size()>0) {
            String[] lineColorsArr = new String[categoryList.size()];
            for (int i = 0; i < categoryList.size(); i++) {
                lineColorsArr[i] = lineColorsArrTmp[i];
            }
            chartsParams.put("lineColors", lineColorsArr);
        }else{
            chartsParams.put("lineColors", lineColorsArrTmp[0]);
        }
        //X轴提示日期
        String[] xLabelsArr = new String[days];
        for (int i = 0; i < days; i++) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            xLabelsArr[i] = sdf.format(new Date((ShopHelper.getCurrentTime()-Long.parseLong(String.valueOf(86400 * (days-i))))*1000L));
        }
        //构造data
        List<HashMap<String, Object>> dataList = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            HashMap<String, Object> dataItem = new HashMap<>();
            dataItem.put("xkey", xLabelsArr[i]);
            if (categoryList!=null && categoryList.size()>0) {
                for (int j = 0; j < categoryList.size(); j++) {
                    //该分类当天的销量
                    dataItem.put("ykeys"+j, 0);
                    for (StatOrdersGoodsVo dayInfo:categoryDayList.get(j)) {
                        if (dayInfo.getCreateTimeShort().equals(xLabelsArr[i])) {
                            dataItem.put("ykeys"+j, dayInfo.getOrdersGoodsPriceSum());
                        }
                    }
                }
                dataList.add(dataItem);
            }else{
                dataItem.put("ykeys0", 0);
                dataList.add(dataItem);
            }
        }
        chartsParams.put("data", dataList);
        chartsMap = ChartsHelper.getMorrisDataLineCharts(chartsParams);
        return chartsMap;
    }

    /**
     * 昨日今日销售趋势图
     */
    @ResponseBody
    @RequestMapping(value = "index/stat/hourtrend", method = RequestMethod.GET)
    public HashMap<String, Object> hourSaleTrend(){
        List<StatOrdersVo> todayList = statOrdersService.getOrdersSalesHourByToday();
        List<StatOrdersVo> yesterdayList = statOrdersService.getOrdersSalesHourByYesterday();
        //构造走势图数据
        HashMap<String, Object> chartsParams = new HashMap<>();
        //走势图Y轴提示商品分类名称
        chartsParams.put("labels", new String[]{"今日","昨日"});
        //走势图Y轴数据key
        chartsParams.put("ykeys", new String[]{"ykeys0","ykeys1"});
        //走势图线条颜色
        chartsParams.put("lineColors", new String[]{ChartsHelper.colorArr.get("blue"),ChartsHelper.colorArr.get("orange")});
        //X轴提示小时
        Integer[] xLabelsArr = new Integer[24];
        for (int i = 0; i < 24; i++) {
            xLabelsArr[i] = i;
        }
        //构造data
        List<HashMap<String, Object>> dataList = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            HashMap<String, Object> dataItem = new HashMap<>();
            dataItem.put("xkey", xLabelsArr[i]);
            //今天数据
            dataItem.put("ykeys0", 0);
            if (todayList!=null && todayList.size()>0) {
                for (StatOrdersVo item:todayList) {
                    if (item.getCreateTimeHour() == i) {
                        dataItem.put("ykeys0", item.getOrdersAmountSum());
                    }
                }
            }
            //昨天数据
            dataItem.put("ykeys1", 0);
            if (yesterdayList!=null && yesterdayList.size()>0) {
                for (StatOrdersVo item:yesterdayList) {
                    if (item.getCreateTimeHour() == i) {
                        dataItem.put("ykeys1", item.getOrdersAmountSum());
                    }
                }
            }
            dataList.add(dataItem);
        }
        chartsParams.put("data", dataList);
        HashMap<String, Object> chartsMap;
        chartsMap = ChartsHelper.getMorrisDataLineCharts(chartsParams);
        return chartsMap;
    }


}