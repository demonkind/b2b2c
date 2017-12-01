package net.shopnc.b2b2c.vo.goods;

/**
 * Created by shopnc.feng on 2015-12-02.
 */
public class SpecValueListVo {
    private int specValueId;
    private String specValueName;
    private String imageSrc;

    public int getSpecValueId() {
        return specValueId;
    }

    public void setSpecValueId(int specValueId) {
        this.specValueId = specValueId;
    }

    public String getSpecValueName() {
        return specValueName;
    }

    public void setSpecValueName(String specValueName) {
        this.specValueName = specValueName;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    @Override
    public String toString() {
        return "SpecValueListVo{" +
                "specValueId=" + specValueId +
                ", specValueName='" + specValueName + '\'' +
                ", imageSrc='" + imageSrc + '\'' +
                '}';
    }
}
