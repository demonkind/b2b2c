package net.shopnc.b2b2c.constant;

import net.shopnc.b2b2c.config.ShopConfig;

/**
 * Created by shopnc.feng on 2015-11-17.
 */
public class UrlAdmin {
    //首页
    public static final String HOME = ShopConfig.getAdminRoot() + "";
    // 品牌列表
    public static final String BRAND_LIST = ShopConfig.getAdminRoot() + "brand/list";
    // 品牌标签列表
    public static final String BRAND_LABEL_LIST = ShopConfig.getAdminRoot() + "brand_label/list";
    // 分类列表
    public static final String CATEGORY_LIST = ShopConfig.getAdminRoot() + "category/list";
    // 规格列表
    public static final String SPEC_LIST = ShopConfig.getAdminRoot() + "spec/list";
    // 会员列表
    public static final String MEMBER_LIST = ShopConfig.getAdminRoot() + "member/list";
    // 权限组列表
    public static final String ADMIN_GROUP_LIST = ShopConfig.getAdminRoot() + "admin_group/list";
    // bycj [ 退款列表 ]
    public static final String REFUND_REASON_LIST = ShopConfig.getAdminRoot() + "refund/reason/list";

        }
