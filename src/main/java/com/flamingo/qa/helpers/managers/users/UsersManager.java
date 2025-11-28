package com.flamingo.qa.helpers.managers.users;

import com.flamingo.qa.Cockpit;
import com.flamingo.qa.backoffice.AdminCockpit;
import com.flamingo.qa.backoffice.user.BackofficeUserRole;
import com.flamingo.qa.helpers.models.users.User;
import com.flamingo.qa.helpers.models.users.UserRole;
import com.flamingo.qa.storefront.StorefrontCockpit;
import com.flamingo.qa.storefront.user.StorefrontUserRole;
import lombok.extern.java.Log;
import org.openqa.selenium.NotFoundException;

import java.util.ArrayList;
import java.util.stream.Stream;

@Log
public class UsersManager {
    protected ArrayList<User> users = new ArrayList<>();

    public User createInstance(String role, String email, String password, Cockpit userCockpit) {
        User user = new User(email, password, userCockpit);
        this.users.add(user);
        user.setUserRole(parseUserRoles(userCockpit, role));
        return user;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public User getUserByEmail(String email) {
        return getUsers().stream().filter(user -> user.getEmail().equalsIgnoreCase(email)).findAny().orElse(null);
    }

    protected UserRole parseUserRoles(Cockpit userCockpit, String userRoleName) {
        if (userCockpit instanceof StorefrontCockpit) {
            return Stream.of(StorefrontUserRole.values())
                    .filter(storefrontCockpitUserRole -> storefrontCockpitUserRole.getRoleName().equalsIgnoreCase(userRoleName))
                    .findAny().orElseThrow(() -> new NotFoundException(String.format("User with role %s for cockpit %s is not found in framework", userRoleName, userCockpit)));
        } else if (userCockpit instanceof AdminCockpit) {
            return Stream.of(BackofficeUserRole.values())
                    .filter(backofficeCockpitUserRole -> backofficeCockpitUserRole.getRoleName().equalsIgnoreCase(userRoleName))
                    .findAny().orElseThrow(() -> new NotFoundException(String.format("User with role %s for cockpit %s is not found in framework", userRoleName, userCockpit)));
        } else {
            throw new NotFoundException(String.format("Cockpit %s is not found in framework", userCockpit));
        }
    }

    public User getUserByCockpit(Cockpit cockpit) {
        return this.users.stream().filter(user -> user.getUserCockpit().equals(cockpit)).findAny()
                .orElseThrow(() -> new NotFoundException("No user found with cockpit: " + cockpit));
    }
}
