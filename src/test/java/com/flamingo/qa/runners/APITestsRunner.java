package com.flamingo.qa.runners;

import io.qameta.allure.junit5.AllureJunit5;
import lombok.extern.java.Log;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.reflect.Method;

@Log
@ExtendWith(AllureJunit5.class)
public class APITestsRunner extends TestsRunner {

    @BeforeEach
    public void beforeMethodLog(TestInfo testInfo) {
        log.info("START TEST METHOD: " + testInfo.getTestMethod().map(Method::getName).orElse("Unknown"));
    }

    @AfterEach
    public void afterMethodCleanUp() {
        bookingsManager.unpickThreadBookings();
    }

    @AfterEach
    public void afterMethodLog(TestInfo testInfo) {
        log.info("FINISH TEST METHOD: " + testInfo.getTestMethod().map(Method::getName).orElse("Unknown"));
    }
}
