package net.shopnc.common.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import net.coobird.thumbnailator.Thumbnails;
import net.shopnc.b2b2c.config.ShopConfig;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class ShopHelper {
    final static Logger logger = Logger.getLogger(ShopHelper.class);

    public static String uploadFile(MultipartFile file) throws Exception {

        if(!ShopConfig.getUploadOpen()) {
            logger.warn("禁止上传文件");
            throw new Exception("禁止上传文件");
        }

        String originalFileName = file.getOriginalFilename();
        String ext = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);

        String imageNameSeed = String.valueOf(System.currentTimeMillis()) + originalFileName;
        String fileName = getMd5(imageNameSeed) + "." + ext.toLowerCase();

        String filePath = "image/" + fileName.substring(0, 2) + "/" + fileName.substring(2, 4) + "/";
        String dir = ShopConfig.getUploadPath().trim() + filePath;
        ShopHelper.createDir(dir);

        File newFile = new File(dir + fileName);
        file.transferTo(newFile);
        return filePath + fileName;
    }

    /**
     * 图片等比例缩放
     */
    public static boolean imageThumb(String filePath, int width, int height) throws Exception {
        //生成缩略图
        Thumbnails.of(filePath).size(width, height).toFile(filePath);
        return true;
    }

    public static boolean imageThumb2(String filePath, int width, int height) throws Exception {
        //用它可以读取丢失ICC信息的图片
        Image image = Toolkit.getDefaultToolkit().getImage(filePath);
        //This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();
        //Determine if the image has transparent pixels; for this method's implementation, see e661 Determining If an Image Has Transparent Pixels boolean hasAlpha = hasAlpha(image); Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
            //throw new HeadlessException("生成失败");
            throw new HeadlessException(e.getMessage());
        }
        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            //int type = BufferedImage.TYPE_3BYTE_BGR;
            /*if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }*/
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }
        //Copy image to buffered image
        Graphics g = bimage.createGraphics();
        //Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();
        //生成缩略图
        Thumbnails.of(bimage).size(width, height).toFile(filePath);
        return true;
    }

    /**
     * 图片裁切
     */
    public static boolean imageCut(String filePath, int width, int height, int x, int y) throws Exception {
        //图片裁切
        Thumbnails.of(filePath).sourceRegion(x, y, width, height).size(width, height).keepAspectRatio(false).toFile(filePath);
        return true;
    }

    public static String getMd5(String str) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.toString());
            return str;
        }
        md.update(str.getBytes());

        byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        //convert the byte to hex format method 2
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            String hex = Integer.toHexString(0xff & byteData[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * 获取当前时间
     */
    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 获取当前时间戳
     */
    public static long getCurrentTime() {
        return System.currentTimeMillis()/1000;
    }
    /**
     * 时间戳转换成日期格式字符串
     * @param seconds 精确到秒的字符串
     * @param formatStr
     * @return
     */
    public static Timestamp time2Timestamp(String seconds, String formatStr) {
        if(seconds == null || seconds.length()<=0){
            return null;
        }
        if(formatStr == null || formatStr.length()<=0){
            formatStr = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        String timestampStr = sdf.format(new Date(Long.valueOf(seconds+"000")));
        return Timestamp.valueOf(timestampStr);
    }
    /**
     * 获取将来时间
     *
     * @param type   Calendar.YEAR(年) Calendar.MONTH(月) Calendar.DATE(日)
     * @param amount 数量，根据type的不同代表几年、几个月、几天
     * @return
     */
    public static Timestamp getFutureTimestamp(int type, int amount) {
        Timestamp timestamp = ShopHelper.getCurrentTimestamp();
        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp);
        cal.add(type, amount);
        return new Timestamp(cal.getTime().getTime());
    }

    /**
     * 获取将来时间
     * @param time 需要添加的时间
     * @param type   Calendar.YEAR(年) Calendar.MONTH(月) Calendar.DATE(日) Calendar.SECOND(秒)
     * @param amount 数量，根据type的不同代表几年、几个月、几天
     * @return
     */
    public static Timestamp getFutureTimestamp(Timestamp time,int type, int amount ){
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.add(type, amount);
        return new Timestamp(cal.getTime().getTime());
    }

    /**
     * 获取一天的0点时间
     */
    public static Timestamp getTimestampOfDayStart(Timestamp time) {
        if (time==null) {
            time = getCurrentTimestamp();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return Timestamp.valueOf(simpleDateFormat.format(time)+" 00:00:00");
    }

    /**
     * 获取一天的23:59:59时间
     */
    public static Timestamp getTimestampOfDayEnd(Timestamp time) {
        if (time==null) {
            time = getCurrentTimestamp();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return Timestamp.valueOf(simpleDateFormat.format(time) + " 23:59:59");
    }

    /**
     * 获取IP
     */
    public static String getAddressIP() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ipAddress = null;
        ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1")) {
                //Based on the IP network card in the machine configuration
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }
        }
        //在通过多个代理的情况下,第一个真正的IP IP为客户,多个IP依照“,”分割,"***.***.***.***".length()
        if (ipAddress != null && ipAddress.length() > 15) {
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

    /**
     * 商品二维码
     *
     * @param goodsId
     * @throws WriterException
     * @throws IOException
     */
    public static void createGoodsQRCode(int storeId, int goodsId) {
        String text = ShopConfig.getWebRoot() + "goods/" + goodsId;
        int width = 100;
        int height = 100;
        String format = "gif";

        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 0);

        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
            String dir = ShopConfig.getUploadPath() + storeId + "/";
            ShopHelper.createDir(dir);
            File outPutFile = new File(dir + goodsId + ".gif");
            MatrixToImageWriter.writeToFile(bitMatrix, format, outPutFile);
        } catch (WriterException e) {
            logger.error(e.toString());
        } catch (IOException e) {
            logger.error(e.toString());
        }

    }

    public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {
            return true;
        }
        //创建目录
        if (dir.mkdirs()) {
            return true;
        } else {
            logger.warn("图片目录创建失败");
            return false;
        }
    }

    /**
     * http GET 请求
     * @param url
     * @return
     * @throws Exception
     */
    public static String httpGet(String url) throws Exception{
        String result = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        //设置最大请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        try {
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity);
                EntityUtils.consume(entity);
            } else {
                throw new Exception(response.getStatusLine().toString());
            }
        } finally {
            response.close();
        }
        return result;
    }

    /**
     * http POST 请求
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public static String httpPost(String url, HashMap<String,Object> params) throws Exception {
        String result = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        //设置最大请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
        httpPost.setConfig(requestConfig);
        //拼接参数
        java.util.List<NameValuePair> nvps = new ArrayList <NameValuePair>();
        for (String key : params.keySet()) {
            nvps.add(new BasicNameValuePair(key,params.get(key).toString()));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        CloseableHttpResponse response = httpClient.execute(httpPost);

        try {
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity);
                EntityUtils.consume(entity);
            } else {
                throw new Exception(response.getStatusLine().toString());
            }
        } finally {
            response.close();
        }
        return result;
    }

    /**
     * 拼接url参数
     * @param params
     * @return
     * @throws Exception
     */
    public static String buildQueryString(HashMap<String, String> params) {
        String queryString = "";
        try {
            int i = 0;
            for (String key : params.keySet()) {
                queryString += (key+"="+java.net.URLEncoder.encode(params.get(key), "utf-8"));
                if (i < params.size()-1) {
                    queryString +="&";
                }
                i++;
            }
        } catch (Exception e) {
        }
        return queryString;
    }


}