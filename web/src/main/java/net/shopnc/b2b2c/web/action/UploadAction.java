package net.shopnc.b2b2c.web.action;

import net.shopnc.b2b2c.service.UploadService;
import net.shopnc.common.entity.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

@Controller
public class UploadAction {
    @Autowired
    UploadService uploadService;

    @ResponseBody
    @RequestMapping(value = "image/upload.json", method = RequestMethod.POST)
    public ResultEntity imageUpload(@RequestParam(value = "file") CommonsMultipartFile file) throws IOException, NoSuchAlgorithmException {
        ResultEntity resultEntity = new ResultEntity();
        try {
            HashMap<String, String> map = uploadService.adminUpload(file);
            resultEntity.setData(map);
            
            resultEntity.setCode(ResultEntity.SUCCESS);
            resultEntity.setMessage("上传成功");
        } catch (Exception e) {
        	e.printStackTrace();
            resultEntity.setCode(ResultEntity.FAIL);
            resultEntity.setMessage(e.getMessage());
        }
        return resultEntity;
    }
}
