package com.project.qa.ui.suites;

import org.junit.platform.suite.api.*;

@Suite
@IncludeEngines("cucumber")
@ConfigurationParameters({
        @ConfigurationParameter(key = "allure.junit5.enabled", value = "false"),
        @ConfigurationParameter(key = "cucumber.features", value = "classpath:features/student"),
        @ConfigurationParameter(
                key = "cucumber.glue",
                value = "com.project.qa.steps, com.project.qa.runners"
        ),
        @ConfigurationParameter(key = "cucumber.plugin",
                value = "pretty, html:target/cucumber-html-reports/report.html, io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm")
})
public class CreateStudentSuite {
}
