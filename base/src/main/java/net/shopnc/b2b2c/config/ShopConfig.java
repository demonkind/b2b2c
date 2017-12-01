package net.shopnc.b2b2c.config;

/**
 * Created by dqw on 2015/10/12.
 */
public class ShopConfig {
    private static String host;

    private static String webRoot;

    private static String adminRoot;

    private static String memberRoot;

    private static String sellerRoot;

    private static String uploadPath;

    private static String uploadRoot;

    private static String publicRoot;

    private static Boolean cacheOpen = false;

    private static Boolean searchOpen = false;

    private static String searchUrl = "";

    private static Boolean uploadOpen = true;

    public static String getHost() {
        return host;
    }

    public static void setHost(String host) {
        ShopConfig.host = host;
    }

    public static String getWebRoot() {
        return webRoot;
    }

    public static void setWebRoot(String webRoot) {
        ShopConfig.webRoot = webRoot;
    }

    public static String getAdminRoot() {
        return adminRoot;
    }

    public static void setAdminRoot(String adminRoot) {
        ShopConfig.adminRoot = adminRoot;
    }

    public static String getMemberRoot() {
        return memberRoot;
    }

    public static void setMemberRoot(String memberRoot) {
        ShopConfig.memberRoot = memberRoot;
    }

    public static String getSellerRoot() {
        return sellerRoot;
    }

    public static void setSellerRoot(String sellerRoot) {
        ShopConfig.sellerRoot = sellerRoot;
    }

    public static String getUploadPath() {
        return uploadPath;
    }

    public static void setUploadPath(String uploadPath) {
        ShopConfig.uploadPath = uploadPath;
    }

    public static String getUploadRoot() {
        return uploadRoot;
    }

    public static void setUploadRoot(String uploadRoot) {
        ShopConfig.uploadRoot = uploadRoot;
    }

    public static String getPublicRoot() {
        return publicRoot;
    }

    public static void setPublicRoot(String publicRoot) {
        ShopConfig.publicRoot = publicRoot;
    }

    public static Boolean getCacheOpen() {
        return cacheOpen;
    }

    public static void setCacheOpen(Boolean cacheOpen) {
        ShopConfig.cacheOpen = cacheOpen;
    }

    public static Boolean getSearchOpen() {
        return searchOpen;
    }

    public static void setSearchOpen(Boolean searchOpen) {
        ShopConfig.searchOpen = searchOpen;
    }

    public static String getSearchUrl() {
        return searchUrl;
    }

    public static void setSearchUrl(String searchUrl) {
        ShopConfig.searchUrl = searchUrl;
    }

    public static Boolean getUploadOpen() {
        return uploadOpen;
    }

    public static void setUploadOpen(Boolean uploadOpen) {
        ShopConfig.uploadOpen = uploadOpen;
    }
}
