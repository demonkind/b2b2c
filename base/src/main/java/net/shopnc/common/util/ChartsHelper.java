package net.shopnc.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zxy on 2016-02-04
 */
public class ChartsHelper {
    /**
     * 多颜色图表
     */
    public static HashMap<String,String> colorArr = new HashMap<String,String>(){
        {
            put("blue","#348fe2");
            put("blueLight","#5da5e8");
            put("blueDark","#1993E4");
            put("aqua","#49b6d6");
            put("aquaLight","#6dc5de");
            put("aquaDark","#3a92ab");
            put("green","#00acac");
            put("greenLight","#33bdbd");
            put("greenDark","#008a8a");
            put("orange","#f59c1a");
            put("orangeLight","#f7b048");
            put("orangeDark","#c47d15");
            put("dark","#2d353c");
            put("grey","#b6c2c9");
            put("purple","#727cb6");
            put("purpleLight","#8e96c5");
            put("purpleDark","#5b6392");
            put("red","#ff5b57");
        }
    };

    /**
     * 获得morris.js LineCharts走势统计图数据
     * @param params 图表参数
     * @return 图表Map数据
     */
    public static HashMap<String, Object> getMorrisDataLineCharts(HashMap<String, Object> params){
        //获得统计Json
        HashMap<String, Object> chartsMap = new HashMap<>();
        chartsMap.put("data", params.get("data"));
        //X轴对应data数据key
        chartsMap.put("xkey", "xkey");
        //y轴对应data数据key,数组形式
        chartsMap.put("ykeys", params.get("ykeys"));
        //tip,数组形式
        chartsMap.put("labels", params.get("labels"));
        //改变不透明度的区域填充颜色。接受之间的值0.0(完全透明的)和1.0(完全不透明的)
        chartsMap.put("fillOpacity", 0.6);
        chartsMap.put("xLabelAngle", 35);
        //鼠标悬屏显示tip
        chartsMap.put("hideHover", "auto");
        chartsMap.put("resize", true);
        chartsMap.put("lineColors", params.get("lineColors"));
        return chartsMap;
    }

    /**
     * 获得Highcharts Column2D柱状统计图数据
     * @param params 图表参数
     * @return 图表Map数据
     */
    public static HashMap<String, Object> getHighchartsDataColumn2D(HashMap<String, Object> params){
        //指定绘制区所要绘制的图的类型，例如：type=bar为柱图，type=line为线图
        HashMap<String, Object> chartConfig = new HashMap<>();
        chartConfig.put("type", "column");
        //去除版权信息
        HashMap<String, Object> creditsConfig = new HashMap<>();
        creditsConfig.put("enabled", false);
        //导出功能选项
        HashMap<String, Object> exportingConfig = new HashMap<>();
        exportingConfig.put("enabled", false);
        //图例开关设置
        HashMap<String, Object> legendConfig = new HashMap<>();
        legendConfig.put("enabled", false);
        //标题，如果为字符串则使用默认样式
        HashMap<String, Object> titleConfig = new HashMap<>();
        if (params.get("title_text")!=null && params.get("title_text").toString().length()>0) {
            titleConfig.put("text", "<b>"+params.get("title_text")+"</b>");
            titleConfig.put("x", -10);
        }
        //子标题如果为字符串则使用默认样式
        HashMap<String, Object> subtitleConfig = new HashMap<>();
        if (params.get("subtitle_text")!=null && params.get("subtitle_text").toString().length()>0) {
            subtitleConfig.put("text", "<b>" + params.get("subtitle_text") + "</b>");
            subtitleConfig.put("x", -10);
        }
        //Y轴如果为字符串则使用默认样式
        HashMap<String, Object> yAxisConfig = new HashMap<>();
        if (params.get("yAxis_Title_Text")!=null && params.get("yAxis_Title_Text").toString().length()>0) {
            HashMap<String,Object> yAxisTitleConfig = new HashMap<>();
            //title文字
            yAxisTitleConfig.put("text", params.get("yAxis_Title_Text").toString());
            //title样式
            HashMap<String,String> yAxisTitleStyleConfig = new HashMap<>();
            yAxisTitleStyleConfig.put("color", "#707478");
            yAxisTitleConfig.put("style", yAxisTitleStyleConfig);
            yAxisConfig.put("title", yAxisTitleConfig);
        }
        //X轴类别名称
        HashMap<String, Object> xAxisConfig = new HashMap<>();
        xAxisConfig.put("categories", params.get("xAxis_categories"));
        //各项统计值(仅支持单柱形)
        List<Object> seriesConfig = new ArrayList<>();
        HashMap<String, Object> seriesItemConfig = new HashMap<>();
        seriesItemConfig.put("name", params.get("series_name"));
        seriesItemConfig.put("data", params.get("series_data"));
        seriesConfig.add(seriesItemConfig);
        //获得统计Json
        HashMap<String, Object> chartsMap = new HashMap<>();
        chartsMap.put("chart", chartConfig);
        chartsMap.put("credits", creditsConfig);
        chartsMap.put("exporting", exportingConfig);
        chartsMap.put("legend", legendConfig);
        chartsMap.put("title", titleConfig);
        chartsMap.put("subtitle", subtitleConfig);
        chartsMap.put("yAxis", yAxisConfig);
        chartsMap.put("xAxis", xAxisConfig);
        chartsMap.put("series", seriesConfig);
        return chartsMap;
    }

    /**
     * 获得morris.js BarChart柱状统计图数据
     * @param params 图表参数
     * @return 图表Map数据
     */
    public static HashMap<String, Object> getMorrisDataBarChart(HashMap<String, Object> params){
        //获得统计Json
        HashMap<String, Object> chartsMap = new HashMap<>();
        chartsMap.put("data", params.get("data"));
        //X轴对应data数据key
        chartsMap.put("xkey", "xkey");
        //y轴对应data数据key
        chartsMap.put("ykeys", new String[]{"ykeys"});
        //tip
        chartsMap.put("labels", new String[]{params.get("ylabel").toString()});
        //改变不透明度的区域填充颜色。接受之间的值0.0(完全透明的)和1.0(完全不透明的)
        chartsMap.put("fillOpacity", 0.6);
        chartsMap.put("xLabelAngle", 35);
        //鼠标悬屏显示tip
        chartsMap.put("hideHover", "auto");
        chartsMap.put("resize", true);
        chartsMap.put("barColors", chartsMap.get("dark"));
        return chartsMap;
    }
}
