package net.shopnc.b2b2c.constant;

import java.util.HashMap;

/**
 * 品牌显示形式<br/>
 * Created by shopnc.feng on 2016-01-04.
 */
public class BrandShowType {
    /**
     * 未推荐
     */
    public static final int TEXT = 0;
    /**
     * 已推荐
     */
    public static final int IMAGE = 1;

    public HashMap<String, Object> get() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("text", BrandShowType.TEXT);
        map.put("image", BrandShowType.IMAGE);
        return map;
    }
}
