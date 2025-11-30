package com.flamingo.qa.storefront.pages;

import com.flamingo.qa.storefront.StorefrontBasePage;
import io.qameta.allure.Step;
import org.springframework.stereotype.Component;

import javax.security.auth.Subject;
import java.time.LocalDate;

@Component
public class StudentRegistrationPage extends StorefrontBasePage {
    private final String PAGE_URL = "order-management/";

    @Step("Fill in firstname field with: {0}" )
    public void fillInFirstNameField(String firstName) {


    }

    @Step("Fill in lastname field with: {0}" )
    public void fillInLastNameField(String lastName) {


    }

    @Step("Fill in email field with: {0}" )
    public void fillInEmailField(String email) {


    }

    @Step("Select gender: {0}" )
    public void selectGenderRadiobutton(Gender gender) {


    }

    @Step("Fill in phone number field with: {0}" )
    public void fillInPhoneNumberField(String phoneNumber) {


    }

    @Step("Select date of birth: {0}" )
    public void selectDateOfBirth(LocalDate dateOfBirth) {


    }

    @Step("Select subject: {0}" )
    public void selectSubject(Subject subject) {


    }

    @Step("Select hobbies: {0}" )
    public void selectHobbiesCheckbox(List<Hobby> hobbies) {


    }

    @Step("Select hobby: {0}" )
    public void selectHobbyCheckbox(Hobby hobby) {


    }

    @Step("Upload picture" )
    public void uploadPicture(File file) {


    }

    @Step("Fill in address field with: {0}" )
    public void fillInAddress(String address) {


    }

    @Step("Select city: {0}" )
    public void selectCity(City city) {


    }

    @Step("Select state: {0}" )
    public void selectState(State state) {


    }

    @Override
    public String getPageUrl() {
        return storefrontCockpit.getBaseUrl() + PAGE_URL;
    }
}
