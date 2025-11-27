package edu.uni.edumatrix.util.commons;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RequestDataHolder {
    private String requestHash;
    public String getRequestHash() {
        if(StringUtils.isBlank(requestHash)) {
            requestHash = Generator.randomAlphaNumeric(6);
        }
        return requestHash;
    }

    public void setRequestHash(String requestHash) {
        this.requestHash = requestHash;
    }
}
