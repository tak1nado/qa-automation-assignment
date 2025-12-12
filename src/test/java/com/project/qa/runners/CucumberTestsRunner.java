package com.project.qa.runners;

import com.project.qa.helpers.PlaywrightRemoteHubSettings;
import com.project.qa.helpers.web.engine.BrowserSetups;
import com.project.qa.helpers.web.engine.BrowserThreadTestSetups;
import io.cucumber.testng.FeatureWrapper;
import io.cucumber.testng.PickleWrapper;
import io.cucumber.testng.TestNGCucumberRunner;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.testng.ITestContext;
import org.testng.annotations.*;

@Log
@ContextConfiguration(locations = {"classpath:spring-application-context.xml"})
public class CucumberTestsRunner extends TestsRunner {
    @Autowired protected BrowserThreadTestSetups browserThreadTestSetups;
    private TestNGCucumberRunner testNGCucumberRunner;

    @Value("${browser.name:chrome}")
    private String browserName;
    @Value("${browser.headless.mode:true}")
    private String headless;

    @Autowired private PlaywrightRemoteHubSettings playwrightRemoteHubSettings;

    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return testNGCucumberRunner.provideScenarios();
    }

    @Test(
            groups = "cucumber",
            description = "Runs Cucumber Scenarios",
            dataProvider = "scenarios"
    )
    public void runScenario(PickleWrapper pickleWrapper, FeatureWrapper featureWrapper) {
        testNGCucumberRunner.runScenario(pickleWrapper.getPickle());
    }

    @BeforeClass(alwaysRun = true)
    public void setUpClass(ITestContext context) throws ClassNotFoundException {
        String browserName = System.getProperty("browserName", this.browserName).toUpperCase();
        String headless = System.getProperty("headless", this.headless);

        browserThreadTestSetups.setTlBrowserSetups(
                new BrowserSetups(playwrightRemoteHubSettings.getHubUrl(), browserName, headless)
        );

//        String className = context.getCurrentXmlTest()
//                .getXmlClasses()
//                .get(0)
//                .getName();
//
//        Class<?> realClass = Class.forName(className);

        testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        if (testNGCucumberRunner != null) {
            testNGCucumberRunner.finish();
        }
    }
}
