package net.shopnc.b2b2c.seller.action;

import net.shopnc.b2b2c.constant.FilesType;
import net.shopnc.b2b2c.dao.store.AlbumFilesDao;
import net.shopnc.b2b2c.domain.store.AlbumFiles;
import net.shopnc.b2b2c.seller.util.SellerSessionHelper;
import net.shopnc.common.entity.PageEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 图片空间
 * Created by shopnc.feng on 2015-12-09.
 */
@Controller
public class AlbumAction extends BaseAction {
    @Autowired
    AlbumFilesDao albumFilesDao;

    public AlbumAction() {
        HashMap<String, String> tabMenuMap = new LinkedHashMap<String, String>();
        tabMenuMap.put("album/list", "图片空间");
        setSellerTabMenu(tabMenuMap);
        setMenuPath("album/list");
    }

    /**
     * 图片列表
     * @param page
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "album/list", method = RequestMethod.GET)
    public String list(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                       ModelMap modelMap) {
        PageEntity pageEntity = new PageEntity();
        pageEntity.setTotal(albumFilesDao.findCountByStoreId(SellerSessionHelper.getStoreId(), FilesType.GOODS));
        pageEntity.setPageSize(16);
        pageEntity.setPageNo(page);
        List<AlbumFiles> albumFilesList = albumFilesDao.findListByStoreId(SellerSessionHelper.getStoreId(), FilesType.GOODS, pageEntity.getPageNo(), pageEntity.getPageSize());

        modelMap.put("albumFilesList", albumFilesList);
        modelMap.put("showPage",pageEntity.getPageHtml());

        return getSellerTemplate("store/album/list");
    }
}
