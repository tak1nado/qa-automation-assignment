package com.flamingo.qa.helpers.web.engine;

import com.flamingo.qa.helpers.managers.users.UsersManager;
import com.flamingo.qa.helpers.models.users.UserRole;
import com.flamingo.qa.helpers.user.engine.UserSessions;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.stqa.selenium.factory.LooseWebDriverPool;
import ru.stqa.selenium.factory.WebDriverPool;

import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class WebDriverSessions {
    @Autowired UserSessions userSessions;
    @Autowired WebDriverThreadTestSetups webDriverThreadTestSetups;
    @Autowired UsersManager usersManager;

    private final InheritableThreadLocal<HashMap<UserRole, WebDriverSession>> tlDriversMap = new InheritableThreadLocal<>();

    private final InheritableThreadLocal<WebDriverPool> tlWebDriverPool = new InheritableThreadLocal<>();

    public synchronized void setDriver(URL hubUrl, String browserName, boolean headless, UserRole userRole) {
        Capabilities capabilities;
        if (browserName.equalsIgnoreCase("chrome")) {
            capabilities = new ChromeOptions();

            //Turn off webdriver local logs: [SEVERE]: Timed out receiving message from renderer: ...
            System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");
            Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);

            LoggingPreferences logPrefs = new LoggingPreferences();
            logPrefs.enable(LogType.BROWSER, Level.ALL);
            logPrefs.enable(LogType.DRIVER, Level.SEVERE);
            logPrefs.enable(LogType.PERFORMANCE, Level.SEVERE);

            ((ChromeOptions) capabilities).setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
            ((ChromeOptions) capabilities).addArguments("--remote-allow-origins=*");

            HashMap<String, Object> chromePrefs = new HashMap<>();
            chromePrefs.put("plugins.always_open_pdf_externally", true);
            ((ChromeOptions) capabilities).setExperimentalOption("prefs", chromePrefs);

            if (headless) {
                ((ChromeOptions) capabilities).addArguments("--headless");
                ((ChromeOptions) capabilities).addArguments("window-size=1920x1080");
            }
        } else {
            capabilities = new DesiredCapabilities();
            ((DesiredCapabilities) capabilities).setBrowserName(browserName);
        }

        if (tlWebDriverPool.get() == null) {
            tlWebDriverPool.set(new LooseWebDriverPool());
        }
        WebDriver webDriver = tlWebDriverPool.get().getDriver(hubUrl, capabilities);
        tlDriversMap.get().put(userRole, new WebDriverSession(webDriver));
        webDriver.get(usersManager.getUsers()
                .stream()
                .filter(user -> user.getUserRole().equals(userRole))
                .findFirst()
                .orElseThrow(() -> new NullPointerException("No created users with this role: " + userRole.toString()))
                .getUserCockpit().getBaseUrl());
    }

    public synchronized void setDriverActive(UserRole userRole) {
        if (tlDriversMap.get() == null) {
            tlDriversMap.set(new HashMap<>());
            setDriver(webDriverThreadTestSetups.getWebDriverSetups().getHubUrl(),
                    webDriverThreadTestSetups.getWebDriverSetups().getBrowserName(),
                    webDriverThreadTestSetups.getWebDriverSetups().isHeadless(),
                    userRole);
            tlDriversMap.get().get(userRole).setActive(true);
            userSessions.setActiveUserSession(userRole);
        } else if (!userSessions.getActiveUserSession().getUserRole().equals(userRole)) {
            tlDriversMap.get().forEach((userRole1, webDriverSession) -> webDriverSession.setActive(false));
            if (tlDriversMap.get().get(userRole) == null)
                setDriver(webDriverThreadTestSetups.getWebDriverSetups().getHubUrl(),
                        webDriverThreadTestSetups.getWebDriverSetups().getBrowserName(),
                        webDriverThreadTestSetups.getWebDriverSetups().isHeadless(),
                        userRole);
            tlDriversMap.get().get(userRole).setActive(true);
            userSessions.setActiveUserSession(userRole);
        }
        userSessions.getActiveUserSession().setCookies(tlDriversMap.get().get(userRole).getWebDriver().manage().getCookies());
    }

    public synchronized WebDriver getActiveDriver() {
        return tlDriversMap.get().values().stream().filter(WebDriverSession::isActive).map(WebDriverSession::getWebDriver).findAny().orElse(null);
    }

    public synchronized WebDriverSession getActiveDriverSession() {
        return tlDriversMap.get().values().stream().filter(WebDriverSession::isActive).findAny().orElse(null);
    }

    public void dismissAll() {
        if (tlDriversMap.get() != null) {
            tlDriversMap.get().values().forEach(WebDriverSession::dismiss);
            tlDriversMap.get().clear();
        }
    }

    public DriverInfo getDriverInfo() {
        var driver = (RemoteWebDriver) getActiveDriver();
        return new DriverInfo(driver.getCapabilities().getBrowserName(), driver.getCapabilities().getBrowserVersion());
    }
}
