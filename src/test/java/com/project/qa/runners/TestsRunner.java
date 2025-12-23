package com.project.qa.runners;

import com.project.qa.api.controller.actions.AuthActions;
import com.project.qa.backoffice.user.BackofficeUserRole;
import com.project.qa.helpers.managers.bookings.BookingsManager;
import com.project.qa.helpers.user.engine.UserSession;
import com.project.qa.helpers.web.engine.WebDriverThreadTestSetups;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import lombok.extern.java.Log;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.PostConstruct;

@Log
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:spring-application-context.xml"})
public class TestsRunner {
    @Autowired protected WebDriverThreadTestSetups webDriverThreadTestSetups;
    @Autowired protected BookingsManager bookingsManager;
    @Autowired private AuthActions authActions;
    @Autowired private ConfigurableApplicationContext applicationContext;
    private static boolean initContext = false;
    protected UserSession adminUserSession;

    @PostConstruct
    public void initSuite() {
        RestAssured.replaceFiltersWith(new AllureRestAssured());
        adminUserSession = authActions.loginAs(BackofficeUserRole.ADMIN);
        if (!initContext) {
            GlobalCleanupListener.setApplicationContext(applicationContext);
            initContext = true;
        }
    }
}
