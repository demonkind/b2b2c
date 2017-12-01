package net.shopnc.common.config;

import freemarker.cache.TemplateLoader;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.List;

/**
 * Created by dqw on 2016/3/2.
 */
public class ShopNCFreeMarkerConfigurer extends FreeMarkerConfigurer {
    @Override
    protected TemplateLoader getAggregateTemplateLoader(List<TemplateLoader> templateLoaders) {
        logger.info("TemplateLoader");
        return new HtmlTemplateLoader(super.getAggregateTemplateLoader(templateLoaders));
    }
}
