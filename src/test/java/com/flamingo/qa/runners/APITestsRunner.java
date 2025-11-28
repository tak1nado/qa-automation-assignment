package com.flamingo.qa.runners;

import com.flamingo.qa.helpers.managers.users.UsersManager;
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
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:spring-application-context.xml"})
public class APITestsRunner {
    @Autowired private UsersManager usersManager;

    @BeforeAll
    static void initSuite() {
        RestAssured.replaceFiltersWith(new AllureRestAssured());
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
//        usersManager.unpickThreadUsers();
    }

    @AfterEach
    public void afterMethodLog(TestInfo testInfo) {
        log.info("FINISH TEST METHOD: " + testInfo.getTestMethod().map(Method::getName).orElse("Unknown"));
    }

    @AfterAll
    public void cleanUp() {
//        usersManager.deleteAllCreatedUsers();
        tearDownClass();
    }

    public void tearDownClass() {
        createAllureReportEnvironmentVariables();
    }

    private void createAllureReportEnvironmentVariables() {
        try (FileOutputStream fos = new FileOutputStream("target/allure-results/environment.properties")) {
            Properties properties = new Properties();
//            properties.setProperty("Environment: ", ); //TODO add base url data
            ofNullable(System.getProperty("os.name")).ifPresent(p -> properties.setProperty("OS name: ", p));
            properties.store(fos, EMPTY);
        } catch (IOException e) {
            log.warning("IO problem when writing allure properties file: " + e.getMessage());
        }
    }
}
