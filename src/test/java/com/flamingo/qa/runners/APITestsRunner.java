package com.flamingo.qa.runners;

import com.flamingo.qa.api.controller.BookerController;
import com.flamingo.qa.api.controller.actions.AuthActions;
import com.flamingo.qa.backoffice.user.BackofficeUserRole;
import com.flamingo.qa.helpers.managers.bookings.BookingsManager;
import com.flamingo.qa.helpers.user.engine.UserSession;
import com.flamingo.qa.storefront.StorefrontCockpit;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import lombok.extern.java.Log;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Log
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:spring-application-context.xml"})
public class APITestsRunner {
    @Autowired private BookingsManager bookingsManager;
    @Autowired private AuthActions authActions;
    @Autowired private BookerController bookerController;
    @Autowired private StorefrontCockpit storefrontCockpit;

    protected UserSession adminUserSession;

    @BeforeAll
    public void initSuite() {
        RestAssured.replaceFiltersWith(new AllureRestAssured());
        adminUserSession = authActions.loginAs(BackofficeUserRole.ADMIN);
    }

    @BeforeEach
    public void beforeMethodLog(TestInfo testInfo) {
        log.info("START TEST METHOD: " + testInfo.getTestMethod().map(Method::getName).orElse("Unknown"));
        preconditions();
    }

    public void preconditions() {
    }

    @AfterEach
    public void afterMethodCleanUp() {
        bookingsManager.unpickThreadBookings();
    }

    @AfterEach
    public void afterMethodLog(TestInfo testInfo) {
        log.info("FINISH TEST METHOD: " + testInfo.getTestMethod().map(Method::getName).orElse("Unknown"));
    }

    @AfterAll
    public void cleanUp() {
        bookingsManager.deleteAllCreatedBookings(adminUserSession);
        createAllureReportEnvironmentVariables();
    }

    private void createAllureReportEnvironmentVariables() {
        try (FileOutputStream fos = new FileOutputStream("target/allure-results/environment.properties")) {
            Properties properties = new Properties();
            properties.setProperty("Environment: ", storefrontCockpit.getBaseUrl());
            properties.setProperty("Restful endpoint: ", bookerController.getBaseUrl());
            ofNullable(System.getProperty("os.name")).ifPresent(p -> properties.setProperty("OS name: ", p));
            properties.store(fos, EMPTY);
        } catch (IOException e) {
            log.warning("IO problem when writing allure properties file: " + e.getMessage());
        }
    }
}
