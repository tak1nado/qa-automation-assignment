package com.project.qa.helpers.web.engine;

import lombok.Getter;

import java.net.URL;

public class WebDriverSetups {
    @Getter private final URL hubUrl;
    @Getter private final String browserName;
    @Getter private final boolean headless;

    public WebDriverSetups(URL hubUrl, String browserName, String headless) {
        this.hubUrl = hubUrl;
        this.browserName = browserName;
        this.headless = Boolean.parseBoolean(headless);
    }
}
