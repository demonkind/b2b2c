package net.shopnc.b2b2c.seller.util;

import net.shopnc.b2b2c.domain.store.SellerMenu;
import net.shopnc.b2b2c.domain.store.StoreGrade;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

import java.util.List;
import java.util.Map;

/**
 * Created by dqw on 2015/12/8.
 */
public class SellerSessionHelper {
    /**
     * session前缀
     */
    private static String prefix = "ncSession_";

    public static int getSellerId() {
        Session session = SecurityUtils.getSubject().getSession();
        return (Integer) session.getAttribute(prefix + "sellerId");
    }

    public static void setSellerId(int sellerId) {
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute(prefix + "sellerId", sellerId);
    }

    public static String getSellerName() {
        Session session = SecurityUtils.getSubject().getSession();
        return (String) session.getAttribute(prefix + "sellerName");
    }

    public static void setSellerName(String sellerName) {
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute(prefix + "sellerName", sellerName);
    }

    public static String getSellerAvatarUrl() {
        Session session = SecurityUtils.getSubject().getSession();
        return (String) session.getAttribute(prefix + "sellerAvatar");
    }

    public static void setSellerAvatar(String sellerAvatar) {
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute(prefix + "sellerAvatar", sellerAvatar);
    }

    public static int getStoreId() {
        Session session = SecurityUtils.getSubject().getSession();
        return (Integer) session.getAttribute(prefix + "storeId");
    }

    public static void setStoreId(int storeId) {
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute(prefix + "storeId", storeId);
    }

    public static String getStoreName() {
        Session session = SecurityUtils.getSubject().getSession();
        return (String) session.getAttribute(prefix + "storeName");
    }

    public static void setStoreName(String storeName) {
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute(prefix + "storeName", storeName);
    }

    public static int getIsOwnShop() {
        Session session = SecurityUtils.getSubject().getSession();
        return (Integer) session.getAttribute(prefix + "isOwnShop");
    }

    public static void setIsOwnShop(int isOwnShop) {
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute(prefix + "isOwnShop", isOwnShop);
    }

    public static int getStoreGoodsLimit() {
        Session session = SecurityUtils.getSubject().getSession();
        return (Integer) session.getAttribute(prefix + "goodsLimit");
    }

    public static void setStoreGoodsLimit(int goodsLimit) {
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute(prefix + "goodsLimit", goodsLimit);
    }

    public static int getStoreAlbumLimit() {
        Session session = SecurityUtils.getSubject().getSession();
        return (Integer) session.getAttribute(prefix + "albumLimit");
    }

    public static void setStoreAlbumLimit(int albumLimit) {
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute(prefix + "albumLimit", albumLimit);
    }

    public static int getStoreCommendLimit() {
        Session session = SecurityUtils.getSubject().getSession();
        return (Integer) session.getAttribute(prefix + "recommendLimit");
    }

    public static void setStoreCommendLimit(int recommendLimit) {
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute(prefix + "recommendLimit", recommendLimit);
    }

    public static String getError() {
        Session session = SecurityUtils.getSubject().getSession();
        return (String) session.getAttribute(prefix + "error");
    }

    public static void setError(String error) {
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute(prefix + "error", error);
    }

    public static Map<String, List<SellerMenu>> getSellerMenu() {
        Session session = SecurityUtils.getSubject().getSession();
        return (Map<String, List<SellerMenu>>) session.getAttribute(prefix + "sellerMenu");
    }

    public static void setSellerMenu(Map<String, List<SellerMenu>> sellerMenu) {
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute(prefix + "sellerMenu", sellerMenu);
    }
}
