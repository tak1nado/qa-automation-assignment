package com.flamingo.qa.storefront.page_blocks;

import com.flamingo.qa.helpers.web.page.UIComponent;
import io.qameta.allure.Step;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StudentRegistrationFormModal extends UIComponent {

    @Step("Get firstname")
    public String getFirstName() {
        return getText();
    }

    @Step("Get lastname")
    public String getLastName() {
        return getText();
    }

    @Step("Get email")
    public String getEmailField() {
        return getText();
    }

    @Step("Get gender")
    public String getGender() {
        return getText();
    }

    @Step("Get phone number")
    public String getPhoneNumber() {
        return getText();
    }

    @Step("Get date of birth")
    public String getDateOfBirth() {
        return getText();
    }

    @Step("Gte subjects")
    public List<String> getSubjects() {
        return getTexts();
    }

    @Step("Select hobbies")
    public List<String> getHobbies() {
        return getTexts();
    }

    @Step("Get address")
    public String getAddress() {
        return getText();
    }

    @Step("Get city")
    public String getCity() {
        return getText();
    }

    @Step("Get state")
    public String getState() {
        return getText();
    }

    @Step("Close Student submission pop-up")
    public void closeModal() {

    }

    public boolean isOpened() {
        return ;
    }
}
