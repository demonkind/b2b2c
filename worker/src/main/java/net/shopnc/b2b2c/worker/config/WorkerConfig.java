package net.shopnc.b2b2c.worker.config;

/**
 * Created by shopnc.feng on 2016-02-23.
 */
public class WorkerConfig {
    private static String uri;
    private static String softwareSerialNo;
    private static String key;

    public static String getUri() {
        return uri;
    }

    public static void setUri(String uri) {
        WorkerConfig.uri = uri;
    }

    public static String getSoftwareSerialNo() {
        return softwareSerialNo;
    }

    public static void setSoftwareSerialNo(String softwareSerialNo) {
        WorkerConfig.softwareSerialNo = softwareSerialNo;
    }

    public static String getKey() {
        return key;
    }

    public static void setKey(String key) {
        WorkerConfig.key = key;
    }
}
