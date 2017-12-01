package net.shopnc.b2b2c.vo.goods;

import net.shopnc.b2b2c.domain.goods.GoodsImage;

/**
 * Created by shopnc.feng on 2015-12-04.
 */
public class GoodsPicVo {
    /**
     * 颜色规格值编号<br>
     * 编号为1的规格对应的规格值的编号
     */
    private int colorId;
    /**
     * 图片名称
     */
    private String imageName;
    /**
     * 图片排序
     */
    private int imageSort;
    /**
     * 默认主图<br>
     * 1是，0否
     */
    private int isDefault;

    public GoodsPicVo() {
    }

    public GoodsPicVo(GoodsImage goodsImage) {
        this.colorId = goodsImage.getColorId();
        this.imageName = goodsImage.getImageName();
        this.imageSort = goodsImage.getImageSort();
        this.isDefault = goodsImage.getIsDefault();
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getImageSort() {
        return imageSort;
    }

    public void setImageSort(int imageSort) {
        this.imageSort = imageSort;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public String toString() {
        return "GoodsPicVo{" +
                "colorId=" + colorId +
                ", imageName='" + imageName + '\'' +
                ", imageSort=" + imageSort +
                ", isDefault=" + isDefault +
                '}';
    }
}
