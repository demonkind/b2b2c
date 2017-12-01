package net.shopnc.b2b2c.vo.admin;

import java.util.List;

/**
 * Created by dqw on 2015/12/10.
 */
public class AdminMenuStateVo {
    private int currentMenu1;
    private int currentMenu2;
    private int currentMenu3;
    private List<String> BreadCrumbList;

    public int getCurrentMenu1() {
        return currentMenu1;
    }

    public void setCurrentMenu1(int currentMenu1) {
        this.currentMenu1 = currentMenu1;
    }

    public int getCurrentMenu2() {
        return currentMenu2;
    }

    public void setCurrentMenu2(int currentMenu2) {
        this.currentMenu2 = currentMenu2;
    }

    public int getCurrentMenu3() {
        return currentMenu3;
    }

    public void setCurrentMenu3(int currentMenu3) {
        this.currentMenu3 = currentMenu3;
    }

    public List<String> getBreadCrumbList() {
        return BreadCrumbList;
    }

    public void setBreadCrumbList(List<String> breadCrumbList) {
        BreadCrumbList = breadCrumbList;
    }

    @Override
    public String toString() {
        return "AdminMenuStateVo{" +
                "currentMenu1='" + currentMenu1 + '\'' +
                ", currentMenu2='" + currentMenu2 + '\'' +
                ", currentMenu3='" + currentMenu3 + '\'' +
                ", BreadCrumbList=" + BreadCrumbList +
                '}';
    }
}

