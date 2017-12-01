package net.shopnc.common.config;

import freemarker.cache.TemplateLoader;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Created by dqw on 2016/3/2.
 */
public class HtmlTemplateLoader implements TemplateLoader {
    public static final String ESCAPE_PREFIX = "<#escape x as x?html>";
    public static final String ESCAPE_SUFFIX = "</#escape>";

    private final TemplateLoader delegate;

    public HtmlTemplateLoader(TemplateLoader delegate) {
        this.delegate = delegate;
    }

    @Override
    public Object findTemplateSource(String name) throws IOException {
        return delegate.findTemplateSource(name);
    }

    @Override
    public long getLastModified(Object templateSource) {
        return delegate.getLastModified(templateSource);
    }

    @Override
    public Reader getReader(Object templateSource, String encoding) throws IOException {
        Reader reader = delegate.getReader(templateSource, encoding);
        try {
            String templateText = IOUtils.toString(reader);
            return new StringReader(ESCAPE_PREFIX + templateText + ESCAPE_SUFFIX);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    @Override
    public void closeTemplateSource(Object templateSource) throws IOException {
        delegate.closeTemplateSource(templateSource);
    }
}
