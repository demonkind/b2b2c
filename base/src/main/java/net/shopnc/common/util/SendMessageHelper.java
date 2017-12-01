package net.shopnc.common.util;

import net.shopnc.b2b2c.constant.MessageTemplateSendType;
import net.shopnc.b2b2c.vo.message.SendMessageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * Created by shopnc.feng on 2016-02-03.
 */
@Component
public class SendMessageHelper {
    @Autowired
    private QueueHelper queueHelper;

    /**
     * 加入队列
     * @param number
     * @param content
     */
    public void pushQueueSms(String number, String content) {
        SendMessageVo sendMessage = new SendMessageVo();
        sendMessage.setNumber(number);
        sendMessage.setContent(content);
        sendMessage.setSendType(MessageTemplateSendType.SMS);
        String jsonString = JsonHelper.toJson(sendMessage);
        queueHelper.push(jsonString);
    }

    /**
     * 加入队列
     * @param number
     * @param title
     * @param content
     */
    public void pushQueueEmail(String number, String title, String content) {
        SendMessageVo sendMessage = new SendMessageVo();
        sendMessage.setNumber(number);
        sendMessage.setTitle(title);
        sendMessage.setContent(content);
        sendMessage.setSendType(MessageTemplateSendType.EMAIL);
        String jsonString = JsonHelper.toJson(sendMessage);
        queueHelper.push(jsonString);
    }

    /**
     * 发送消息
     * @param sendMessage
     */
    public void send(SendMessageVo sendMessage) {
        System.out.println(sendMessage);
    }

    /**
     * 模板字符串替换
     * @param string
     * @param map
     * @return
     *
     *
     *       String string = "<p>您好！</p>\n" +
     *       "<p>您刚才在{$site_name}重置了密码，新密码为：{$new_password}。</p>\n" +
     *       "<p>请尽快登录 <a href=\"{$site_url}\" target=\"_blank\">{$site_url}</a> 修改密码。</p>";
     *       HashMap<String, Object> map = new HashMap<>();
     *       map.put("site_name", "B2B2C JAVA版");
     *       map.put("new_password", "1101101111110");
     *       map.put("site_url", "http://www.shopnc.net");
     *       String stringNew = sendMessageHelper.replace(string, map);
     *       System.out.println(stringNew);
     */
    public String replace(String string, HashMap<String, Object> map) {
        if (string == null || string.length() == 0) {
            return "";
        }
        for (String key : map.keySet()) {
            string = string.replace("{$" + key + "}", String.valueOf(map.get(key)));
        }
        return string;
    }
}
