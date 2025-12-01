package com.flamingo.qa.runners;

import com.flamingo.qa.helpers.ThreadVarsHashMap;
import com.flamingo.qa.helpers.web.engine.WebDriverSessions;
import com.flamingo.qa.steps.TestKeyword;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import lombok.extern.java.Log;
import org.junit.platform.suite.api.SelectClasses;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayInputStream;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Log
@SelectClasses(CucumberTestsRunner.class)
public class CucumberHooks {
    @Autowired private WebDriverSessions webDriverPool;
    @Autowired private ThreadVarsHashMap<TestKeyword> threadVarsHashMap;

    @Value("#{new Boolean('${console.errors:false}'.trim())}")
    boolean consoleErrors;

    @After(order = 2)
    public synchronized void onFailure(Scenario scenario) {
        if (scenario.isFailed()) {
            captureScreenshot(scenario);
            attachBrowserLogs();
        }
    }

    @After(order = 3)
    public synchronized void checkForBrowserErrors() {
        if(consoleErrors) {
        LogEntries logEntries = attachBrowserLogs();
        assertTrue(logEntries.getAll()
                .stream()
                .noneMatch(f -> f.getLevel().getName().equalsIgnoreCase("SEVERE")));
        }
    }

    @After(order = 1)
    public synchronized void clearThreadVars() {
        threadVarsHashMap.clear();
    }

    public void captureScreenshot(Scenario scenario) {
        try {
            Allure.addAttachment(scenario.getName(), new ByteArrayInputStream(((TakesScreenshot) webDriverPool.getActiveDriver())
                    .getScreenshotAs(OutputType.BYTES)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String formatLogs(LogEntries logEntries) {
        return logEntries
                .getAll()
                .stream()
                .filter(f -> f.getLevel().getName().equalsIgnoreCase("SEVERE"))
                .map(LogEntry::toString).collect(Collectors.joining("\n--------------\n"));
    }

    private LogEntries attachBrowserLogs() {
        WebDriver driver = webDriverPool.getActiveDriver();
        LogEntries logEntries = null;
        if (driver instanceof ChromeDriver) {
            logEntries = driver.manage().logs().get(LogType.BROWSER);
            if (!logEntries.getAll().isEmpty()) {
                Allure.attachment("Browser logs: ", formatLogs(logEntries));
            }
        }
        return logEntries;
    }
}