package com.project.qa.helpers.web.engine;

import org.springframework.stereotype.Component;

@Component
public class WebDriverThreadTestSetups {

    private final InheritableThreadLocal<WebDriverSetups> tlDriverSetups = new InheritableThreadLocal<>();

    public synchronized void setTlDriverSetups(WebDriverSetups webDriverSetups) {
        tlDriverSetups.set(webDriverSetups);
    }

    public synchronized WebDriverSetups getWebDriverSetups() {
        return tlDriverSetups.get();
    }
}
