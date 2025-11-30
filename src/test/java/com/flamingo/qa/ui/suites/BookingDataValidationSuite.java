package com.flamingo.qa.ui.suites;

import com.flamingo.qa.runners.CucumberTestsRunner;
import org.junit.jupiter.api.Tag;
import org.junit.platform.suite.api.*;

@Suite
@IncludeEngines("cucumber")
@Tag("UI")
@SelectClasses(CucumberTestsRunner.class)
@ConfigurationParameters({
        @ConfigurationParameter(key = "cucumber.features", value = "classpath:features/bookings"),
        @ConfigurationParameter(
                key = "cucumber.glue",
                value = "com.flamingo.qa.steps, com.flamingo.qa.runners"
        ),
        @ConfigurationParameter(key = "cucumber.plugin",
                value = "pretty, html:target/cucumber-html-reports/report.html, io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm")
})
public class BookingDataValidationSuite {
}
