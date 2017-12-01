package net.shopnc.b2b2c.service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by shopnc.feng on 2015-11-06.
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class BaseService {
    protected final Logger logger = Logger.getLogger(getClass());
}
