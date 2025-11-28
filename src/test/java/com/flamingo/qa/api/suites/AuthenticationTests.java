package com.flamingo.qa.api.suites;

import com.flamingo.qa.backoffice.user.BackofficeUserRole;
import com.flamingo.qa.helpers.managers.users.UsersManager;
import com.flamingo.qa.helpers.models.users.User;
import com.flamingo.qa.runners.APITestsRunner;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


@Log
@Epic("Authentication")
@Feature("Restful authentication feature")
public class AuthenticationTests extends APITestsRunner {
    @Autowired private UsersManager usersManager;

    @Test
    @Description("Check that Admin is able to authenticate.")
    public void userAuthResponseValidation() {
        User admin = usersManager.getDefaultUserByRole(BackofficeUserRole.ADMIN);

        log.info("Test user: " + admin);

    }
}
