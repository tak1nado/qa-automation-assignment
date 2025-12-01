package com.flamingo.qa.runners;

import com.flamingo.qa.helpers.SeleniumGridSettings;
import com.flamingo.qa.helpers.web.engine.WebDriverSetups;
import io.cucumber.spring.CucumberContextConfiguration;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

@Log
@CucumberContextConfiguration
public class CucumberTestsRunner extends TestsRunner {

    @Value("${browser.name:chrome}")
    private String browserName;
    @Value("${browser.headless.mode:true}")
    private String headless;

    @Autowired private SeleniumGridSettings seleniumGridSettings;

    @PostConstruct
    public void initUISuite() {
        String browserName = System.getProperty("browserName", this.browserName).toUpperCase();
        String headless = System.getProperty("headless", this.headless);

        webDriverThreadTestSetups.setTlDriverSetups(
                new WebDriverSetups(seleniumGridSettings.getHubUrl(), browserName, headless)
        );
        WebDriverManager.getInstance(DriverManagerType.valueOf(browserName)).setup();
    }
}
