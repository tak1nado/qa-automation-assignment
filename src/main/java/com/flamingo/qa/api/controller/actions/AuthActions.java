package com.flamingo.qa.api.controller.actions;

import com.flamingo.qa.api.controller.managers.AuthenticationControllerApi;
import com.flamingo.qa.helpers.managers.users.UsersManager;
import com.flamingo.qa.helpers.models.users.User;
import com.flamingo.qa.helpers.models.users.UserRole;
import com.flamingo.qa.helpers.user.engine.UserSession;
import com.flamingo.qa.helpers.user.engine.UserSessions;
import io.qameta.allure.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthActions {
    @Autowired private UserSessions userSessions;
    @Autowired private UsersManager usersManager;
    @Autowired private AuthenticationControllerApi authenticationControllerApi;

    @Step("Login as user: {0}")
    public synchronized UserSession loginAs(User user) {
        UserSession userSession = userSessions.getUserSession(user);
        if (userSession.getToken() == null || !userSession.getToken().isValid()) {
            authenticationControllerApi.loginRequestForUserSession(userSession);
        }
        return userSession;
    }

    @Step("Login as user with role: {0}")
    public synchronized UserSession loginAs(UserRole userRole) {
        User user = usersManager.getUserByRole(userRole);
        UserSession userSession = userSessions.getUserSession(user);
        if (userSession.getToken() == null || !userSession.getToken().isValid()) {
            authenticationControllerApi.loginRequestForUserSession(userSession);
        }
        return userSession;
    }

    public synchronized String getAccessToken(UserSession userSession) {
        if (!userSession.getToken().isValid()) {
            authenticationControllerApi.loginRequestForUserSession(userSession);
        }
        return userSession.getToken().getAccessToken();
    }
}
