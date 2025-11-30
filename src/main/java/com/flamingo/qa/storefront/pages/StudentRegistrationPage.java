package com.flamingo.qa.storefront.pages;

import com.flamingo.qa.helpers.models.student.*;
import com.flamingo.qa.storefront.StorefrontBasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import static com.flamingo.qa.storefront.page_elements.StudentRegistrationFormPageElements.*;

@Component
public class StudentRegistrationPage extends StorefrontBasePage {
    private final String PAGE_URL = "order-management/";

    @Step("Fill in firstname field with: {0}")
    public void fillInFirstNameField(String firstName) {
        enterText(firstName, By.id(FIRSTNAME_FIELD_ID));
    }

    @Step("Fill in lastname field with: {0}")
    public void fillInLastNameField(String lastName) {
        enterText(lastName, By.id(LAStNAME_FIELD_ID));
    }

    @Step("Fill in email field with: {0}")
    public void fillInEmailField(String email) {
        enterText(email, By.id(EMAIL_FIELD_ID));
    }

    @Step("Select gender: {0}")
    public void selectGenderRadiobutton(Gender gender) {
        click(GENDER_BY_VALUE_RADIOBUTTON_XPATH, gender.genderText);
    }

    @Step("Fill in phone number field with: {0}")
    public void fillInPhoneNumberField(String phoneNumber) {
        enterText(phoneNumber, By.id(PHONE_NUMBER_FIELD_ID));
    }

    @Step("Select date of birth: {0}")
    public void selectDateOfBirth(LocalDate dateOfBirth) {
        click(DATE_OF_BIRTH_FIELD_ID);
        // TODO add select birthday logic
    }

    @Step("Select subjects: {0}")
    public void selectSubjects(List<Subject> subjects) {
        subjects.forEach(this::selectSubject);
    }

    @Step("Select subject: {0}")
    public void selectSubject(Subject subject) {
        enterText(subject.subjectText, By.id(SUBJECTS_FIELD_ID));
        //TODO add logic to select subject from suggestions
    }

    @Step("Select hobbies: {0}")
    public void selectHobbiesCheckbox(List<Hobby> hobbies) {
        hobbies.forEach(this::selectHobbyCheckbox);
    }

    @Step("Select hobby: {0}")
    public void selectHobbyCheckbox(Hobby hobby) {
        click(HOBBIES_BY_VALUE_CHECKBOX_XPATH, hobby.hobbyText);
    }

    @Step("Upload picture")
    public void uploadPicture(File file) {
        enterText(file.getAbsolutePath(), By.id(UPLOAD_PICTURE_INPUT_ID));
    }

    @Step("Fill in address field with: {0}")
    public void fillInAddress(String address) {
        enterText(address, By.id(CURRENT_ADDRESS_FIELD_ID));
    }

    @Step("Select city: {0}")
    public void selectCity(City city) {
        click(By.id(CITY_DROP_DOWN_ID));
        //TODO add logic to select city from drop-down by name
    }

    @Step("Select state: {0}")
    public void selectState(State state) {
        click(By.id(STATE_DROP_DOWN_ID));
        //TODO add logic to select city from drop-down by name
    }

    @Step("Click on Submit button")
    public void clickOnSubmitButton() {
        click(By.id(SUBMIT_BUTTON_ID));
    }

    @Override
    public String getPageUrl() {
        return storefrontCockpit.getBaseUrl() + PAGE_URL;
    }
}
