package net.shopnc.b2b2c.constant;

import java.util.HashMap;

/**
 * 店铺相册默认图片类型
 * Created by shopnc.feng on 2015-10-28.
 */
public class FilesType {
    /**
     * 开店图片<br>
     * 不进相册
     */
    public final static int OUTSIDE = 0;
    /**
     * 商品图片
     */
    public final static int GOODS = 1;
    /**
     * 设置图片<br>
     *     如头像，LOGO，BANNER
     */
    public final static int SETTING = 2;

    public HashMap<String, Object> get() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("outside", OUTSIDE);
        map.put("goods", GOODS);
        map.put("setting", SETTING);
        return map;
    }
}
