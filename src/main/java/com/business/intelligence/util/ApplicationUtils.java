package com.business.intelligence.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by yanshi on 2017/3/17.
 */
@Component
public class ApplicationUtils implements ApplicationContextAware {

    private static ApplicationContext context;

    public static void setContext(ApplicationContext context) {
        ApplicationUtils.context = context;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext){
        ApplicationUtils.setContext(applicationContext);
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public static <T> T getBean(Class<T> requiredType) {
        return getContext().getBean(requiredType);
    }
}
