package net.shopnc.b2b2c.service;

import net.shopnc.b2b2c.dao.AdminLogDao;
import net.shopnc.b2b2c.domain.AdminLog;
import net.shopnc.b2b2c.exception.ShopException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 后台操作日志
 * Created by shopnc on 2015/11/18.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class AdminLogService {
    @Autowired
    private AdminLogDao adminLogDao;

    /**
     * 删除三个月之前的记录
     * @return
     * @throws Exception
     */
    public void delAdminLogByThreeMonthAgo() throws Exception{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -3);
        Date date = simpleDateFormat.parse(simpleDateFormat.format(calendar.getTime()));
        adminLogDao.delAdminLogByTime(date);
    }

    /**
     * 单个删除
     * @param logId
     * @throws ShopException
     */
    public void delAdminLog(int logId) throws ShopException {
        if (logId <= 0) {
            throw new ShopException("参数错误");
        }
        adminLogDao.delete(AdminLog.class, logId);
    }
}
