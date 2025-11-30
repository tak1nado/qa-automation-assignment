package com.flamingo.qa.steps;

import com.flamingo.qa.backoffice.user.BackofficeUserRole;
import com.flamingo.qa.helpers.managers.users.UsersManager;
import com.flamingo.qa.helpers.web.engine.WebDriverSessions;
import com.flamingo.qa.storefront.user.StorefrontUserRole;
import io.cucumber.java.en.Given;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

//@CucumberContextConfiguration
//@ContextConfiguration(locations = {"classpath:spring-application-context.xml"})
public class UserSwitchStepDefs extends AbstractStepDefs {

    @Autowired private WebDriverSessions webDriverPool;
    @Autowired private UsersManager usersManager;

    @Given("Switch to Backoffice cockpit Admin user.")
    public void switchToBackofficeAdmin() {
        webDriverPool.setDriverActive(BackofficeUserRole.ADMIN);
    }

    @Given("Switch to Storefront cockpit Shopper user.")
    public void switchToStorefrontAsShopper() {
        webDriverPool.setDriverActive(StorefrontUserRole.SHOPPER);
    }

    @Given("Switch to Storefront cockpit guest user.")
    public void switchToStorefrontAsGuest() {
        webDriverPool.setDriverActive(StorefrontUserRole.GUEST);
    }
}
