package net.shopnc.b2b2c.vo.seller;

import java.util.List;

/**
 * Created by dqw on 2015/12/03.
 */
public class SellerMenuStateVo {
    private String currentMainMenu;
    private String currentMenuTitle;
    private String currentSubMenu;
    private String currentPath;
    private List<String> BreadCrumbList;

    public String getCurrentMainMenu() {
        return currentMainMenu;
    }

    public void setCurrentMainMenu(String currentMainMenu) {
        this.currentMainMenu = currentMainMenu;
    }

    public String getCurrentMenuTitle() {
        return currentMenuTitle;
    }

    public void setCurrentMenuTitle(String currentMenuTitle) {
        this.currentMenuTitle = currentMenuTitle;
    }

    public String getCurrentSubMenu() {
        return currentSubMenu;
    }

    public void setCurrentSubMenu(String currentSubMenu) {
        this.currentSubMenu = currentSubMenu;
    }

    public String getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(String currentPath) {
        this.currentPath = currentPath;
    }

    public List<String> getBreadCrumbList() {
        return BreadCrumbList;
    }

    public void setBreadCrumbList(List<String> breadCrumbList) {
        BreadCrumbList = breadCrumbList;
    }

    @Override
    public String toString() {
        return "SellerMenuStateVo{" +
                "currentMainMenu='" + currentMainMenu + '\'' +
                ", currentMenuTitle='" + currentMenuTitle + '\'' +
                ", currentSubMenu='" + currentSubMenu + '\'' +
                ", currentPath='" + currentPath + '\'' +
                ", BreadCrumbList=" + BreadCrumbList +
                '}';
    }
}

