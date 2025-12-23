package com.project.qa.steps;

import com.project.qa.backoffice.user.BackofficeUserRole;
import com.project.qa.helpers.managers.users.UsersManager;
import com.project.qa.helpers.web.engine.WebDriverSessions;
import com.project.qa.storefront.user.StorefrontUserRole;
import io.cucumber.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;

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
