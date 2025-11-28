package com.flamingo.qa.helpers.user.engine;

import com.flamingo.qa.helpers.models.users.UserRole;
import io.restassured.http.Cookies;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.Cookie;
import uk.co.mwtestconsultancy.CookieAdapter;
import uk.co.mwtestconsultancy.ExpiryType;

import java.util.Set;
import java.util.stream.Collectors;

abstract public class Session {
    @Getter protected boolean isLoggedIn = false;
    @Getter @Setter protected boolean isActive = false;
    @Getter protected Set<Cookie> cookies;
    @Getter @Setter protected Token token;

    abstract public UserRole getUserRole();

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public void addCookies(Set<Cookie> cookies) {
        Set<Cookie> addCookies = cookies.stream()
                .filter(cookie1 -> this.cookies.stream().noneMatch(cookie2 -> cookie1.getName().equals(cookie2.getName())))
                .collect(Collectors.toSet());

        if (!addCookies.isEmpty())
            this.cookies.addAll(addCookies);
    }

    public void setCookies(Set<Cookie> cookies) {
        if (this.cookies == null) {
            this.cookies = cookies;
        } else
            addCookies(cookies);
    }

    public Cookies getRestAssuredCookies() {
        CookieAdapter cookieAdapter = new CookieAdapter(ExpiryType.MAXAGE);
        return new Cookies(this.cookies.stream().map(cookieAdapter::convertToRestAssured).collect(Collectors.toList()));
    }

    public void clearCookies() {
        this.cookies = null;
    }
}
