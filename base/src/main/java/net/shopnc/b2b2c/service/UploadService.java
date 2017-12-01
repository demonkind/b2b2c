package net.shopnc.b2b2c.service;

import net.shopnc.b2b2c.config.ShopConfig;
import net.shopnc.b2b2c.constant.FilesType;
import net.shopnc.b2b2c.dao.store.AlbumFilesDao;
import net.shopnc.b2b2c.domain.store.AlbumFiles;
import net.shopnc.common.util.ShopHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;

/**
 * Created by shopnc.feng on 2015-12-09.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class UploadService extends  BaseService {
    @Autowired
    AlbumFilesDao albumFilesDao;
    /**
     * 验证扩展名
     * @param originalFileName
     * @throws Exception
     */
    private void checkOriginal(String originalFileName) throws Exception{
        // 取得文件扩展名
        String ext = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);

        if (!ext.equalsIgnoreCase("png") && !ext.equalsIgnoreCase("gif") && !ext.equalsIgnoreCase("jpg")) {
            throw new Exception("请上传png、gif、jpg格式的图片");
        }
    }

    /**
     * 图片上传
     * @param file
     * @return
     * @throws Exception
     */
    private String upload(MultipartFile file) throws Exception{
        try {
            return ShopHelper.uploadFile(file);
        } catch (Exception e) {
            // 捕获异常
            logger.error(e.toString());
            throw new Exception("上传失败");
        }
    }

    /**
     * 商家图片上传
     * @param fileIO
     * @param type
     * @param storeId
     * @return
     * @throws Exception
     */
    public HashMap<String, String> sellerUpload(MultipartFile fileIO, int type, int storeId, int fileLimit) throws Exception {
        long count = albumFilesDao.findCountByStoreId(storeId);
        if(fileLimit > 0 && count >= fileLimit ) {
            throw new Exception("图片已经超出可上传图片最大数量");
        }

        BufferedImage sourceImg;
        long size;
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(fileIO.getBytes());
            size = fileIO.getSize();
            sourceImg = ImageIO.read(in);
        } catch (IOException e) {
            logger.error(e.toString());
            throw new Exception("参数错误");
        }
        if (type != FilesType.OUTSIDE && type != FilesType.GOODS && type != FilesType.SETTING ){
            throw new Exception("参数错误");
        }

        checkOriginal(fileIO.getOriginalFilename());
        // 上传图片
        String name = upload(fileIO);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("url", ShopConfig.getUploadRoot() + name);
        map.put("name", name);
        if (type != FilesType.OUTSIDE) {
            Timestamp currTime = new Timestamp(System.currentTimeMillis());
            // 插入相册
            AlbumFiles albumFiles = new AlbumFiles();
            albumFiles.setFilesName(name);
            albumFiles.setUploadTime(currTime);
            albumFiles.setStoreId(storeId);
            albumFiles.setFilesType(type);
            albumFiles.setFilesHeight(sourceImg.getHeight());
            albumFiles.setFilesWidth(sourceImg.getWidth());
            albumFiles.setFilesSize(size);
            albumFilesDao.save(albumFiles);
        }
        return map;
    }

    /**
     * 后台图片上传
     * @param fileIO
     * @return
     * @throws Exception
     */
    public HashMap<String, String> adminUpload(MultipartFile fileIO) throws Exception{
        checkOriginal(fileIO.getOriginalFilename());
        // 上传图片
        String name = upload(fileIO);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("url", ShopConfig.getUploadRoot() + name);
        map.put("name", name);
        return map;
    }

    /**
     * 图片上传
     * @param fileIO
     * @return
     * @throws Exception
     */
    public HashMap<String, String> memberUpload(MultipartFile fileIO) throws Exception{
        checkOriginal(fileIO.getOriginalFilename());
        // 上传图片
        String name = upload(fileIO);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("url", ShopConfig.getUploadRoot() + name);
        map.put("path", ShopConfig.getUploadPath() + name);
        map.put("name", name);
        return map;
    }

}
