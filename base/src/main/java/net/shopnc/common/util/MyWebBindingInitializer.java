package net.shopnc.common.util;

/**
 * Created by zxy on 2015-11-20.
 */
import java.text.SimpleDateFormat;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

public class MyWebBindingInitializer implements WebBindingInitializer {

    //@Override
    public void initBinder(WebDataBinder binder, WebRequest request) {
        SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd");
        datetimeFormat.setLenient(false);
        binder.registerCustomEditor(java.sql.Timestamp.class, new CustomTimestampEditor(datetimeFormat, true));
    }

}