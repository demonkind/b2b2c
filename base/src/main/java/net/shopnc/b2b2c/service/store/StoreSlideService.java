package net.shopnc.b2b2c.service.store;

import net.shopnc.b2b2c.constant.Common;
import net.shopnc.b2b2c.dao.store.StoreSlideDao;
import net.shopnc.b2b2c.domain.store.StoreSlide;
import net.shopnc.b2b2c.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dqw on 2016/01/19.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class StoreSlideService extends BaseService {

    @Autowired
    private StoreSlideDao storeSlideDao;

    public List<StoreSlide> findByStoreId(int storeId) {
        List<StoreSlide> storeSlideList = storeSlideDao.findByStoreId(storeId);
        if(storeSlideList.size() <= 0) {
            StoreSlide storeSlide1 = new StoreSlide();
            storeSlide1.setImageUrl(Common.DEFAULT_STORE_SLIDE1);
            StoreSlide storeSlide2 = new StoreSlide();
            storeSlide2.setImageUrl(Common.DEFAULT_STORE_SLIDE2);
            StoreSlide storeSlide3 = new StoreSlide();
            storeSlide3.setImageUrl(Common.DEFAULT_STORE_SLIDE3);
            StoreSlide storeSlide4 = new StoreSlide();
            storeSlide4.setImageUrl(Common.DEFAULT_STORE_SLIDE4);
            storeSlideList.add(storeSlide1);
            storeSlideList.add(storeSlide2);
            storeSlideList.add(storeSlide3);
            storeSlideList.add(storeSlide4);
        }
        return storeSlideList;
    }

    public void saveByStoreId(int storeId, List<String> slideImgList, List<String> slideUrlList) {
        List<StoreSlide> storeSlideList = new ArrayList<StoreSlide>();
        for(int i = 0; i < 5; i++) {
            String img = slideImgList.get(i);
            String url = slideUrlList.get(i);
            if(img != null && !img.equals("")) {
                StoreSlide storeSlide = new StoreSlide();
                storeSlide.setStoreId(storeId);
                storeSlide.setImage(img);
                storeSlide.setUrl(url);
                storeSlideList.add(storeSlide);
            }
        }
        storeSlideDao.delByStoreId(storeId);
        storeSlideDao.saveAll(storeSlideList);
    }

}

