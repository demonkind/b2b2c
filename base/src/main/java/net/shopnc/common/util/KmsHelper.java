package net.shopnc.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import net.sf.json.JSONObject;

public class KmsHelper {
	private static final String URL = GetProperties.getValue("kmsUrl");
//	private static final String URL	= "http://172.16.103.197:8081/kms/";

	/**
	 * 向网关发请求
	 * @param xml
	 * @throws IOException 
	 */
	public static JSONObject sendToKms(HashMap<String, Object> paramMap, String action) throws IOException{
		String url= URL + action;
		
		System.out.println("sendToKms:  start ");
		JSONObject jsonResult = new JSONObject();
	    HttpResponse response =null;
	    
	    Iterator i = paramMap.entrySet().iterator();
	    List<org.apache.http.NameValuePair> values = new ArrayList<org.apache.http.NameValuePair>();
	    while(i.hasNext()){
	    	Entry entry = (Entry)i.next();
	        String key = entry.getKey().toString();
	        Object obj = entry.getValue();
	        String value = "";
	        if (obj != null){
	        	value = obj.toString();
	        }
	        values.add(new BasicNameValuePair(key, value));
	    }
	    
        try {
        	DefaultHttpClient client = new DefaultHttpClient();
        	HttpPost httpPost = new HttpPost(url);
        	// 参数传递
			httpPost.setEntity(new UrlEncodedFormEntity(values, HTTP.UTF_8));
            response = client.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();  
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity(); 
                String str =null;
                if (entity != null) {    
        	           InputStream instreams = entity.getContent();
        	           BufferedReader br = new BufferedReader(new InputStreamReader(instreams,"utf-8"));
        	           str = br.readLine(); 
        	           httpPost.abort();
                }  
                jsonResult = JSONObject.fromObject(str);
            } else {
            	jsonResult.put("msg","发送POST请求出现异常！");
            	System.out.println("info:"+jsonResult.toString());
            }
        } catch ( Exception e) {
        	e.printStackTrace();
        	jsonResult.put("msg","发送POST请求出现异常！");
        	System.out.println("info:"+jsonResult.toString());
        }
        

		System.out.println("sendBizApi: " + url + " end ");
		
		return jsonResult;
	}

}
