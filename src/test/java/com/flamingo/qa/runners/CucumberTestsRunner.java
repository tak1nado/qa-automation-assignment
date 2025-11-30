package com.flamingo.qa.runners;

import com.flamingo.qa.helpers.SeleniumGridSettings;
import com.flamingo.qa.helpers.user.engine.UserSessions;
import com.flamingo.qa.helpers.web.engine.WebDriverSessions;
import com.flamingo.qa.helpers.web.engine.WebDriverSetups;
import com.flamingo.qa.helpers.web.engine.WebDriverThreadTestSetups;
import io.cucumber.spring.CucumberContextConfiguration;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import lombok.extern.java.Log;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Log
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@CucumberContextConfiguration
@ContextConfiguration(locations = {"classpath:spring-application-context.xml"})
public class CucumberTestsRunner {

    private static final String DEFAULT_BROWSER_NAME = "CHROME";
    private static final String DEFAULT_HEADLESS = "false";

    @Autowired private WebDriverSessions webDriverPool;
    @Autowired private SeleniumGridSettings seleniumGridSettings;
    @Autowired private WebDriverThreadTestSetups webDriverThreadTestSetups;
    @Autowired private UserSessions userSessions;

    @PostConstruct
    public void initSuite() {
        String browserName = System.getProperty("browserName", DEFAULT_BROWSER_NAME).toUpperCase();
        String headless = System.getProperty("headless", DEFAULT_HEADLESS);

        RestAssured.replaceFiltersWith(new AllureRestAssured());

        webDriverThreadTestSetups.setTlDriverSetups(
                new WebDriverSetups(seleniumGridSettings.getHubUrl(), browserName, headless)
        );
        WebDriverManager.getInstance(DriverManagerType.valueOf(browserName)).setup();
    }

    @PreDestroy
    public void finishTestSuit() {
        try {
            if (!userSessions.isSessionsListEmpty()) {
                createAllureReportEnvironmentVariables();
            }
        } finally {
            webDriverPool.dismissAll();
        }
    }

    private void createAllureReportEnvironmentVariables() {
        try (FileOutputStream fos = new FileOutputStream("target/allure-results/environment.properties")) {
            Properties properties = new Properties();
            ofNullable(System.getenv("ENV")).ifPresent(p -> properties.setProperty("Environment: ", p));
            ofNullable(webDriverPool.getDriverInfo().getDriverName()).ifPresent(p -> properties.setProperty("Browser: ", p));
            ofNullable(webDriverPool.getDriverInfo().getDriverVersion()).ifPresent(p -> properties.setProperty("Browser version: ", p));
            ofNullable(System.getProperty("os.name")).ifPresent(p -> properties.setProperty("OS name: ", p));
            ofNullable(System.getenv("GITLAB_USER_NAME")).ifPresent(p -> properties.setProperty("Gitlab user name: ", p));

            properties.store(fos, EMPTY);
        } catch (IOException e) {
            log.warning("IO problem when writing allure properties file: " + e.getMessage());
        }
    }
}
