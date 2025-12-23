package com.project.qa.storefront.page_blocks;

import com.project.qa.helpers.web.page.UIComponent;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.project.qa.storefront.page_block_elements.StudentRegistrationFormModalElements.*;

@Component
public class StudentRegistrationFormModal extends UIComponent {

    @Step("Get full name")
    public String getFullName() {
        return getText(NAME_FIELD_XPATH);
    }

    @Step("Get email")
    public String getEmailField() {
        return getText(EMAIL_FIELD_XPATH);
    }

    @Step("Get gender")
    public String getGender() {
        return getText(GENDER_FIELD_XPATH);
    }

    @Step("Get phone number")
    public String getPhoneNumber() {
        return getText(MOBILE_FIELD_XPATH);
    }

    @Step("Get date of birth")
    public String getDateOfBirth() {
        return getText(DATE_OF_BIRTH_FIELD_XPATH);
    }

    @Step("Get subjects")
    public List<String> getSubjects() {
        return Arrays.stream(getText(SUBJECTS_FIELD_XPATH).split(",\\s*"))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    @Step("Get hobbies")
    public List<String> getHobbies() {
        return Arrays.stream(getText(HOBBIES_FIELD_XPATH).split(",\\s*"))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    @Step("Get picture name")
    public String getPictureName() {
        return getText(PICTURE_FIELD_XPATH);
    }

    @Step("Get address")
    public String getAddress() {
        return getText(ADDRESS_FIELD_XPATH);
    }

    @Step("Get state and city")
    public String getStateCity() {
        return getText(STATE_AND_CITY_FIELD_XPATH);
    }

    @Step("Close Student submission pop-up")
    public void closeModal() {
        click(By.id(CLOSE_BUTTON_ID));
    }

    public boolean isOpened() {
        return isPresent(FORM_POP_UP_BLOCK_XPATH);
    }
}
