package net.shopnc.b2b2c.vo;

/**
 * 面包屑VO
 * Created by shopnc.feng on 2016-01-06.
 */
public class CrumbsVo {
    /**
     * 名称
     */
    private String name;
    /**
     * 路径
     */
    private String url = "javascript:;";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "CrumbsVo{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
