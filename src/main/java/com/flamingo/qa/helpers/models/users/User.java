package com.flamingo.qa.helpers.models.users;

import com.flamingo.qa.Cockpit;
import lombok.Data;

@Data
public class User {
    private UserTitle userTitle;
    private String email;
    private String password;
    private UserRole userRole;
    private Cockpit userCockpit;

    public User(String email, String password, Cockpit userCockpit) {
        this.password = password;
        this.email = email;
        this.userCockpit = userCockpit;
    }

    @Override
    public String toString() {
        return "User: " + this.email + ", Cockpit: " + this.userCockpit.getBaseUrl() + ", Role: " + this.getUserRole();
    }
}
