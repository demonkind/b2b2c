package net.shopnc.common.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;

public class GetProperties {
    private static Map map = null;  
  
    private static void loadFile() {  
        map = new HashMap();  
        try {  
            Properties p = new Properties();  
            p.load(PropertyUtils.class.getClassLoader().getResourceAsStream("db.properties"));  
            Iterator it = p.keySet().iterator();  
            while (it.hasNext()) {  
                String key = (String) it.next();  
                String value = p.getProperty(key);  
                map.put(key, value);  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    public static String getValue(String str) {  
        if (map == null) {  
            loadFile();  
        }  
        return (String) map.get(str);  
    }  
}
