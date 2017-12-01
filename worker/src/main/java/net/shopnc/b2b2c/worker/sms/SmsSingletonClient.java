package net.shopnc.b2b2c.worker.sms;


import net.shopnc.b2b2c.worker.config.WorkerConfig;




public class SmsSingletonClient {
	private static SmsClient client=null;
	private SmsSingletonClient(){
	}
	public synchronized static SmsClient getClient(String uri, String softwareSerialNo,String key){
		if(client==null){
			try {
				client=new SmsClient(uri, softwareSerialNo,key);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return client;
	}
	public synchronized static SmsClient getClient(){
		if(client==null){
			try {
				client=new SmsClient(WorkerConfig.getUri(), WorkerConfig.getSoftwareSerialNo(), WorkerConfig.getKey());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return client;
	}
	
	
}
