package com.flamingo.qa.helpers.web.engine;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;

public class WebDriverSession {
    @Getter private final WebDriver webDriver;
    @Getter private final int timeOut = 30000;
    @Getter private final int shortTimeOut = 1500;
    @Getter private final int mediumTimeOut = 5000;
    @Getter private final int minimumTimeOut = 300;
    @Getter @Setter private boolean isActive = false;
    @Getter private final TimeUnit timeUnit = TimeUnit.MILLISECONDS;

    public WebDriverSession(WebDriver webDriver) {
        this.webDriver = webDriver;
        this.webDriver.manage().window().fullscreen();
        this.webDriver.manage().timeouts().pageLoadTimeout(timeOut, this.timeUnit);
        this.changeImplicitWait(timeOut, this.timeUnit);
    }

    public void restoreDefaultImplicitWait() {
        changeImplicitWait(timeOut, this.timeUnit);
    }

    public void setShortImplicitWait() {
        changeImplicitWait(shortTimeOut, this.timeUnit);
    }

    public void setMediumImplicitWait() {
        changeImplicitWait(mediumTimeOut, this.timeUnit);
    }

    public void setMinimumImplicitWait() {
        changeImplicitWait(minimumTimeOut, this.timeUnit);
    }

    public void changeImplicitWait(int value, TimeUnit timeUnit) {
        webDriver.manage().timeouts().implicitlyWait(value, timeUnit);
    }

    //only for WebDriverSessions usage
    public void dismiss() {
        webDriver.quit();
    }
}
