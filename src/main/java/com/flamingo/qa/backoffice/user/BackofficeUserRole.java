package com.flamingo.qa.backoffice.user;

import com.flamingo.qa.helpers.models.users.UserRole;

public enum BackofficeUserRole implements UserRole {
    ADMIN;

    @Override
    public String getRoleName() {
        return name();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
