package net.shopnc.b2b2c.worker;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import net.shopnc.b2b2c.constant.MessageTemplateSendType;
import net.shopnc.b2b2c.exception.ShopException;
import net.shopnc.b2b2c.vo.message.SendMessageVo;
import net.shopnc.b2b2c.worker.sms.SmsSingletonClient;
import net.shopnc.common.util.EmailHelper;
import net.shopnc.common.util.JsonHelper;
import net.shopnc.common.util.QueueHelper;
import net.shopnc.common.util.SendMessageHelper;

/**
 * Created by dqw on 2016/2/3.
 */
@Component
public class WorkQueue {
    protected final Logger logger = Logger.getLogger(getClass());
    @Autowired
    QueueHelper queueHelper;
    @Autowired
    SendMessageHelper sendMessageHelper;
    @Autowired
    EmailHelper emailHelper;

    public static void main(String[] args) {    	
        ApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext.xml");

        WorkQueue p = context.getBean(WorkQueue.class);
        while (!Thread.currentThread().isInterrupted()) {
            try {
                p.startMessageQueue();
            } catch (Exception ex) {
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    p.logger.info(e.toString());
                }
                p.logger.info(ex.toString());
            }
        }
    }

    private void startMessageQueue() throws ShopException {
        String jsonString = queueHelper.pop();
        if (jsonString == null || jsonString.length() == 0) {
            throw new ShopException("队列暂无数据");
        }
        SendMessageVo sendMessage = (SendMessageVo) JsonHelper.toObject(jsonString, SendMessageVo.class);
      //TODO test
//    	SendMessageVo sendMessage = new SendMessageVo();
//    	sendMessage.setNumber("13818691356");
//    	sendMessage.setContent("测试电商短信通道");
//    	sendMessage.setTitle("Test ec sms");
//    	sendMessage.setSendType(MessageTemplateSendType.SMS);
        logger.info(sendMessage);
        if (sendMessage.getSendType() == MessageTemplateSendType.EMAIL) {
            sendEmail(sendMessage);
        } else if(sendMessage.getSendType() == MessageTemplateSendType.SMS) {
            sendSms(sendMessage);
        }
    }

    private void sendEmail(SendMessageVo sendMessage) {
        emailHelper.sendMail(sendMessage.getTitle(), sendMessage.getContent(), sendMessage.getNumber());
        logger.info("发送了消息到：" + sendMessage.getNumber());
    }

    private void sendSms(SendMessageVo sendMessage) {
        try {
            int i = SmsSingletonClient.getClient().sendSMS(sendMessage.getNumber(), sendMessage.getContent());// 带扩展码
            logger.info("发送了消息到：" + sendMessage.getNumber());
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }
}

