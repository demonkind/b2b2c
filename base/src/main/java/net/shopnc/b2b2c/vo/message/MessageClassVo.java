package net.shopnc.b2b2c.vo.message;

import net.shopnc.b2b2c.domain.MessageTemplateCommon;

import java.util.List;

/**
 * Created by shopnc.feng on 2016-02-19.
 */
public class MessageClassVo {
    private int id;
    private String name;
    private List<MessageTemplateCommon> messageTemplateCommonList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MessageTemplateCommon> getMessageTemplateCommonList() {
        return messageTemplateCommonList;
    }

    public void setMessageTemplateCommonList(List<MessageTemplateCommon> messageTemplateCommonList) {
        this.messageTemplateCommonList = messageTemplateCommonList;
    }

    @Override
    public String toString() {
        return "MessageClassVo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", messageTemplateCommonList=" + messageTemplateCommonList +
                '}';
    }
}
