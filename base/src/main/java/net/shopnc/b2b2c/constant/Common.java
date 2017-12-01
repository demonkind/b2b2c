package net.shopnc.b2b2c.constant;

/**
 * 通用常量集合
 * Created by hbj on 2015/12/3.
 */
public class Common {
    /**
     * 购物车商品存入Cookie时最大存储数量
     */
    public static final int CART_COOKIE_COUNT = 10;
    /**
     * 购物车商品存入数据库时最大存储数量
     */
    public static final long CART_DB_COUNT = 50;
    /**
     * 发送动态码的重发间隔秒数
     */
    public static final int SMS_AUTHCODE_RESEND_TIME = 60;
    /**
     * 动态码的有效秒数
     */
    public static final int SMS_AUTHCODE_VALID_TIME = 600;
    /**
     * 同一手机号24小时内发送动态码最大次数
     */
    public static final int SMS_AUTHCODE_SAMEPHONE_MAXNUM = 20;
    /**
     * 同一IP24小时内，发送动态码最大次数
     */
    public static final int SMS_AUTHCODE_SAMEIP_MAXNUM = 20;
    /**
     * 邮件发送动态码的重发间隔秒数
     */
    public static final int EMAIL_AUTHCODE_RESEND_TIME = 60;
    /**
     * 邮件动态码的有效秒数
     */
    public static final int EMAIL_AUTHCODE_VALID_TIME = 1800;
    /**
     * 邮件同一手机号24小时内发送动态码最大次数
     */
    public static final int EMAIL_AUTHCODE_SAMEPHONE_MAXNUM = 20;
    /**
     * 邮件同一IP24小时内，发送动态码最大次数
     */
    public static final int EMAIL_AUTHCODE_SAMEIP_MAXNUM = 20;
    /**
     * 默认头像路径
     */
    public static final String DEFAULT_AVATAR_URL = "img/avatar.gif";
    /**
     * 默认Logo
     */
    public static final String DEFAULT_SITE_LOGO_URL = "img/logo.png";
    /**
     * 商家中心默认Logo
     */
    public static final String DEFAULT_SELLER_LOGO_URL = "img/seller_logo.png";
    /**
     * 默认登录主题图
     */
    public static final String DEFAULT_LOGIN_IMAGE_URL1 = "img/login_img.jpg";
    public static final String DEFAULT_LOGIN_IMAGE_URL2 = "img/login_img.jpg";
    public static final String DEFAULT_LOGIN_IMAGE_URL3 = "img/login_img.jpg";
    public static final String DEFAULT_LOGIN_IMAGE_URL4 = "img/login_img.jpg";
    /**
     * 默认店铺Logo
     */
    public static final String DEFAULT_STORE_LOGO = "img/default_store_logo.png";
    /**
     * 默认店铺条幅
     */
    public static final String DEFAULT_STORE_BANNER = "img/default_store_banner.png";
    /**
     * 默认店铺头像
     */
    public static final String DEFAULT_STORE_AVATAR = "img/default_store_avatar.png";
    /**
     * 默认店铺幻灯
     */
    public static final String DEFAULT_STORE_SLIDE1 = "img/default_store_slide/f01.jpg";
    public static final String DEFAULT_STORE_SLIDE2 = "img/default_store_slide/f02.jpg";
    public static final String DEFAULT_STORE_SLIDE3 = "img/default_store_slide/f03.jpg";
    public static final String DEFAULT_STORE_SLIDE4 = "img/default_store_slide/f04.jpg";
    /**
     * 每页显示数量
     */
    public static final int PAGESIZE = 20;
    /**
     * 商品默认图
     */
    public static final String DEFAULT_GOODS_IMG = "img/default_goods_image.gif";
    /**
     * 订单超过N小时未支付自动取消
     */
    public static final int ORDER_AUTO_CANCEL_TIME = 1;
    /**
     * 会员最多添加收货地址数量
     */
    public static final int ADDRESS_MAX_NUM = 20;
    /**
     * 会员最多添加发票数量
     */
    public static final int INVOICE_MAX_NUM = 10;
    /**
     * 订单发货后超过N天未收货自动收货
     */
    public static final int ORDER_AUTO_RECEIVE_TIME = 15;
    /**
     * 最大规格数
     */
    public static final int SPEC_MAX_NUM = 5;
    /**
     * 最大规格值数
     */
    public static final int SPEC_VALUE_MAX_NUM = 20;
    /**
     * 订单完成后N天内可以评价
     */
    public static final int ORDER_EVALUATION_MAX_TIME = 15;
    /**
     * 订单完成后3个月内可以追加评价
     */
    public static final int ORDER_EVALUATION_APPEND_MAX_TIME = 3;
}
