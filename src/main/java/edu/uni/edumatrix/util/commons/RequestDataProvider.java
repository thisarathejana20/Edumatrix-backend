package edu.uni.edumatrix.util.commons;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

@Slf4j
@Component
public class RequestDataProvider {
    private final ApplicationContext applicationContext;

    public RequestDataProvider(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public String getRequestHash() {
        if(RequestContextHolder.getRequestAttributes() == null) {
            return "n/a";
        }
        RequestDataHolder requestDataHolder = applicationContext.getBean(RequestDataHolder.class);

        if(requestDataHolder != null) {
            return requestDataHolder.getRequestHash();
        }
        return "n/a";
    }

    public void setRequestHash(String requestHash) {
        if(RequestContextHolder.getRequestAttributes() == null) {
            return;
        }
        RequestDataHolder requestDataHolder = applicationContext.getBean(RequestDataHolder.class);

        if(requestDataHolder != null) {
            requestDataHolder.setRequestHash(requestHash);
        }
    }

}

