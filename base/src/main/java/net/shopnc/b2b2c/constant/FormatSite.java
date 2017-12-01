package net.shopnc.b2b2c.constant;

import java.util.HashMap;

/**
 * 关联板式位置
 * Created by shopnc.feng on 2015-12-17.
 */
public class FormatSite {
    /**
     * 顶部位置
     */
    public static final int TOP = 1;
    /**
     * 底部位置
     */
    public static final int BOTTOM = 0;

    public HashMap<String, Object> get() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("top", TOP);
        map.put("bottom", BOTTOM);
        return map;
    }
}
