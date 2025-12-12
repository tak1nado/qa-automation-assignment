package com.project.qa.ui.suites;

import com.project.qa.runners.CucumberTestsRunner;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "classpath:features/student",
        glue = {"com.project.qa.steps", "com.project.qa.runners", "com.project.qa.runners.CucumberHooks"},
        plugin = {
                "pretty",
                "html:target/cucumber-html-reports/report.html",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        monochrome = true
)
public class CreateStudentSuite extends CucumberTestsRunner {
}
