package com.flamingo.qa.helpers.web.page;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import lombok.extern.java.Log;
import org.openqa.selenium.UnhandledAlertException;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Log
public abstract class BasePageObject extends UIComponent {

    public String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }

    public String getDecodedCurrentUrl() {
        return URLDecoder.decode(getCurrentUrl(), StandardCharsets.UTF_8);
    }

    public boolean isOpened() {
        Allure.step("Is page opened: " + getPageUrl() + "?");
        return getCurrentUrl().equals(getPageUrl());
    }

    public void open() {
        open(getPageUrl());
        waitUntilPageIsFullyLoaded();
    }

    @Step("Open page {0}.")
    protected void open(String url) {
        try {
            getDriver().get(url);
        } catch (UnhandledAlertException ex) {
            log.info("WARNING: Unexpected alert: " + ex);
            alertHandling();
            open(url);
        }
        waitUntilPageIsFullyLoaded();
    }

    public abstract String getPageUrl();

    @Step("Refresh page.")
    public void refreshPage() {
        getDriver().navigate().refresh();
        waitUntilPageIsFullyLoaded();
    }
}