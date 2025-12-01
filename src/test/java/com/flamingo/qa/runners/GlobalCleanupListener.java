package com.flamingo.qa.runners;

import com.flamingo.qa.api.controller.BookerController;
import com.flamingo.qa.api.controller.actions.AuthActions;
import com.flamingo.qa.backoffice.user.BackofficeUserRole;
import com.flamingo.qa.helpers.managers.bookings.BookingsManager;
import com.flamingo.qa.helpers.user.engine.UserSession;
import com.flamingo.qa.helpers.user.engine.UserSessions;
import com.flamingo.qa.helpers.web.engine.WebDriverSessions;
import com.flamingo.qa.storefront.StorefrontCockpit;
import lombok.extern.java.Log;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Log
public class GlobalCleanupListener implements TestExecutionListener {
    private static ConfigurableApplicationContext applicationContext;

    public static void setApplicationContext(ConfigurableApplicationContext context) {
        GlobalCleanupListener.applicationContext = context;
    }

    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        AuthActions authActions = applicationContext.getBean(AuthActions.class);
        BookingsManager bookingsManager = applicationContext.getBean(BookingsManager.class);
        WebDriverSessions webDriverPool = applicationContext.getBean(WebDriverSessions.class);
        UserSession adminUserSession = authActions.loginAs(BackofficeUserRole.ADMIN);
        bookingsManager.deleteAllCreatedBookings(adminUserSession);
        createAllureReportEnvironmentVariables();
        webDriverPool.dismissAll();
    }

    private void createAllureReportEnvironmentVariables() {
        StorefrontCockpit storefrontCockpit = applicationContext.getBean(StorefrontCockpit.class);
        BookerController bookerController = applicationContext.getBean(BookerController.class);
        WebDriverSessions webDriverPool = applicationContext.getBean(WebDriverSessions.class);
        try (FileOutputStream fos = new FileOutputStream("target/allure-results/environment.properties")) {
            Properties properties = new Properties();
            properties.setProperty("Environment: ", storefrontCockpit.getBaseUrl());
            properties.setProperty("Restful endpoint: ", bookerController.getBaseUrl());
            if (webDriverPool.getActiveDriver() != null) {
                ofNullable(webDriverPool.getDriverInfo().getDriverName()).ifPresent(p -> properties.setProperty("Browser: ", p));
                ofNullable(webDriverPool.getDriverInfo().getDriverVersion()).ifPresent(p -> properties.setProperty("Browser version: ", p));
            }
            ofNullable(System.getProperty("os.name")).ifPresent(p -> properties.setProperty("OS name: ", p));
            ofNullable(System.getenv("GITLAB_USER_NAME")).ifPresent(p -> properties.setProperty("Gitlab user name: ", p));

            properties.store(fos, EMPTY);
        } catch (IOException e) {
            log.warning("IO problem when writing allure properties file: " + e.getMessage());
        }
    }
}