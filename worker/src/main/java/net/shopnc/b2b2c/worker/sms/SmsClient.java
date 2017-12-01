package net.shopnc.b2b2c.worker.sms;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class SmsClient {
private String softwareSerialNo;
private String key;
private String uri;
	public SmsClient(String uri, String sn,String key){
		this.softwareSerialNo=sn;
		this.key=key;
		this.uri=uri;
		init();
	}
	
	public void init(){
		
	}

	
	/**
	 * 发送K码给手机客户
	 * @param sendPhone 接收的手机号码
	 * @param sendText
	 */
	public int sendSMS(String sendPhone, String sendText){
		int result;
		String username = softwareSerialNo;//短信宝帐户名
		String pass = new MD5().Md5(key);//短信宝帐户密码，md5加密后的字符串
		String phone = sendPhone;//电话号码
		String content;
		try {
			content = java.net.URLEncoder.encode(sendText,StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			content = sendText ;
			return 1;
		}//发送内容
		httpSend send = new httpSend(uri + "?u="+username+"&p="+pass+"&m="+phone+"&c="+content);
		try {
			result = send.send();
		} catch (Exception e) {
			e.printStackTrace();
			result = 1;
		}
		return result;

	}
}
