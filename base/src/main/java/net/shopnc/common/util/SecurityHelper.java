package net.shopnc.common.util;

import org.owasp.validator.html.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * Cache<br/>
 * Created by dqw on 2016/03/02.
 */
@Component
public class SecurityHelper {

    @Autowired
    private AntiSamy antiSamy;

    private static Policy policy = null;

    private Policy getPolicy() throws PolicyException {
        if (policy == null) {
            InputStream policyFile = getClass().getResourceAsStream("/antisamy-config.xml");
            policy = Policy.getInstance(policyFile);
        }
        return policy;
    }

    /**
     * xss过滤
     * @param html
     * @return
     */
    public String xssClean(String html) {
        try {
            CleanResults cleanResults = antiSamy.scan(html, getPolicy());
            html = cleanResults.getCleanHTML();
        } catch (PolicyException e) {
            e.printStackTrace();
        } catch (ScanException e) {
            e.printStackTrace();
        }
        return html;
    }
}


