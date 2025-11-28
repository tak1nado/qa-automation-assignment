package com.flamingo.qa.helpers.managers.users;

import com.flamingo.qa.Cockpit;
import com.flamingo.qa.backoffice.AdminCockpit;
import com.flamingo.qa.backoffice.user.BackofficeUserRole;
import com.flamingo.qa.helpers.exceptions.InstanceAlreadyPickedException;
import com.flamingo.qa.helpers.models.users.User;
import com.flamingo.qa.helpers.models.users.UserRole;
import com.flamingo.qa.storefront.StorefrontCockpit;
import com.flamingo.qa.storefront.user.StorefrontUserRole;
import lombok.extern.java.Log;
import org.openqa.selenium.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log
public class UsersManager {
    private final ArrayList<User> users = new ArrayList<>();
    private final InheritableThreadLocal<ArrayList<User>> tlUsers = new InheritableThreadLocal<>();
    private final ArrayList<User> pickedUsers = new ArrayList<>();

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
        return getUsers().stream().filter(user -> user.getUsername().equalsIgnoreCase(email)).findAny().orElse(null);
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

    public ArrayList<User> getAllUsers() {
        return users;
    }

    public List<User> getTestUsers() {
        return getAllUsers().stream()
                .filter(User::isTest)
                .collect(Collectors.toList());
    }

    public User getTestUserByRole(UserRole userRole) {
        return this.getTestUsers().stream()
                .filter(user -> user.getUserRole().equals(userRole)).findAny()
                .orElseThrow(() -> new NotFoundException("No user with role: " + userRole));
    }

    public User getDefaultUserByRole(UserRole userRole) {
        return this.getAllUsers().stream()
                .filter(user -> !user.isTest())
                .filter(user -> user.getUserRole().equals(userRole)).findAny()
                .orElseThrow(() -> new NotFoundException("No user with role: " + userRole));
    }

//    public UserData generateRandomValidUserDataWithRole(UserRole userRole) {
//        return new UserData.Builder()
//                .age(randomUtils.getRandomNumberInRange(17, 59))
//                .gender(Gender.getRandom())
//                .login(randomUtils.generateRandomLogin())
//                .password(randomUtils.generateRandomPassword())
//                .role(userRole)
//                .screenName(randomUtils.generateRandomScreenName())
//                .build();
//    }
//
//    public UserData generateRandomValidUserDataWithoutRole() {
//        return new UserData.Builder()
//                .age(randomUtils.getRandomNumberInRange(17, 59))
//                .gender(Gender.getRandom())
//                .login(randomUtils.generateRandomLogin())
//                .password(randomUtils.generateRandomPassword())
//                .screenName(randomUtils.generateRandomScreenName())
//                .build();
//    }
//
//    public void deleteAllCreatedUsers() {
//        log.info("Users to delete: " + getTestUsers());
//        getTestUsers().forEach(this::deleteUser);
//        this.users.removeAll(getTestUsers());
//    }

//    private void deleteUser(User user) {
//        try {
//            User supervisor = getDefaultUserByRole(BackofficeUserRole.ADMIN);
//            bookerControllerUsersApi.deleteUserById(supervisor, user.getId());
//        } catch (UndeclaredThrowableException exception) {
//            log.info("Connection refused: " + exception);
//        }
//    }

    //Pick and unpick functionality needed to leverage users during test execution in parallel mode to avoid data collision
    public synchronized List<User> getNotPickedUsers() {
        return getTestUsers().stream()
                .filter(user -> !pickedUsers.contains(user))
                .collect(Collectors.toList());
    }

    public synchronized User pick(User user) throws InstanceAlreadyPickedException {
        if (!isPicked(user)) {
            this.pickedUsers.add(user);
            if (this.tlUsers.get() == null) {
                this.tlUsers.set(new ArrayList<>());
                this.tlUsers.get().add(user);
            } else if (!tlUsers.get().contains(user)) {
                this.tlUsers.get().add(user);
            }
        }
        return user;
    }

    public boolean isPicked(User user) {
        return this.pickedUsers.contains(user);
    }

    public synchronized void unpickThreadUser(User user) {
        if (this.tlUsers.get() != null) {
            this.pickedUsers.remove(user);
            this.tlUsers.get().remove(user);
        }
    }

    public synchronized void unpickThreadUsers() {
        if (this.tlUsers.get() != null) {
            this.pickedUsers.removeAll(this.tlUsers.get());
            this.tlUsers.get().clear();
        }
    }
}
