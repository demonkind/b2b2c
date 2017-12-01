package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.b2b2c.service.UploadService;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.HashMap;

/**
 * 图片上传
 * Created by shopnc.feng on 2015-11-18.
 */
@Controller
public class UploadJsonAction extends BaseJsonAction {
    @Autowired
    UploadService uploadService;

    /**
     * 图片上传
     * @param file
     * @param type
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "image/upload.json", method = RequestMethod.POST)
    public ResultEntity imageUpload(@RequestParam(value = "file") CommonsMultipartFile file,
                                    int type) {
        ResultEntity resultEntity = new ResultEntity();
        try {
            HashMap<String, String> map = uploadService.sellerUpload(file, type, SellerSessionHelper.getStoreId(), SellerSessionHelper.getStoreAlbumLimit());
            resultEntity.setData(map);
        } catch (Exception e) {
            logger.error(e.toString());
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
            return resultEntity;
        }

        resultEntity.setCode(ResultEntity.SUCCESS);
        resultEntity.setMessage("上传成功");
        return resultEntity;
    }
}
