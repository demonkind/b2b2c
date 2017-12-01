package net.shopnc.b2b2c.vo.message;

/**
 * Created by shopnc.feng on 2016-02-03.
 */
public class SendMessageVo {
    private String number = "";
    private String title = "";
    private String content = "";
    private int sendType = 0;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSendType() {
        return sendType;
    }

    public void setSendType(int sendType) {
        this.sendType = sendType;
    }

    @Override
    public String toString() {
        return "SendMessageVo{" +
                "number='" + number + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", sendType='" + sendType + '\'' +
                '}';
    }
}
