package net.shopnc.b2b2c.domain;

import javax.persistence.*;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 商品评价内容<br>
 * Created by zxy on 2015-10-26
 */
@Entity
@Table(name = "evaluate_goods_content")
public class EvaluateGoodsContent implements Serializable {
    /**
     * 自增编码
     */
    @Id
    @GeneratedValue
    @Column(name="content_id")
    private int contentId;
    /**
     * 评价内容
     */
    @Column(name="content", length = 500)
    private String content;
    /**
     * 解释内容
     */
    @Column(name="explain_content", length = 500)
    private String explainContent;
    /**
     * 晒单图片
     */
    @Column(name="images", length = 500)
    private String images;
    /**
     * 添加时间
     */
    @Column(name="add_time")
    private Timestamp addTime;
    
    /**
     * 评价id 对应evaluate_goods
     */
    @Column(name="evaluate_id")
    private Integer evaluateId;

    public int getEvaluateId() {
		return evaluateId;
	}

	public void setEvaluateId(int evaluateId) {
		this.evaluateId = evaluateId;
	}

	public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExplainContent() {
        return explainContent;
    }

    public void setExplainContent(String explainContent) {
        this.explainContent = explainContent;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }
}
