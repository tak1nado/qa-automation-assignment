package com.project.qa.storefront.page_elements;

public class StudentRegistrationFormPageElements {
    public static final String FIRSTNAME_FIELD_ID = "firstName";
    public static final String LAStNAME_FIELD_ID = "lastName";
    public static final String EMAIL_FIELD_ID = "userEmail";
    public static final String GENDER_BY_VALUE_RADIOBUTTON_XPATH = "//div[@id='genterWrapper']//div[input[@value='%s']]/label";
    public static final String PHONE_NUMBER_FIELD_ID = "userNumber";
    public static final String DATE_OF_BIRTH_FIELD_ID = "dateOfBirthInput";
    public static final String DATE_OF_BIRTH_FORM_MONTH_CSS = ".react-datepicker__month-select";
    public static final String DATE_OF_BIRTH_FORM_YEAR_CSS = ".react-datepicker__year-select";
    public static final String DATE_OF_BIRTH_FORM_PREV_CSS = ".react-datepicker__navigation--previous";
    public static final String DATE_OF_BIRTH_FORM_NEXT_CSS = ".react-datepicker__navigation--next";
    public static final String DATE_OF_BIRTH_FORM_DAY_XPATH = "//div[contains(@class, 'react-datepicker__day') and not(contains(@class, 'outside-month')) and text()='%s']";
    public static final String SUBJECTS_FIELD_ID = "subjectsInput";//"//div[@id='subjectsContainer']/div";
    public static final String SUBJECT_VALUE_SUGGESTIONS_DOWN_BY_NAME_XPATH = "//div[@id='subjectsContainer']//div[@tabindex and .='%s']";
    public static final String HOBBIES_BY_VALUE_CHECKBOX_XPATH = "//div[@id='hobbiesWrapper']//label[.='%s']";
    public static final String UPLOAD_PICTURE_INPUT_ID = "uploadPicture";
    public static final String CURRENT_ADDRESS_FIELD_ID = "currentAddress";
    public static final String STATE_DROP_DOWN_ID = "state";
    public static final String STATE_VALUE_SUGGESTIONS_DOWN_BY_NAME_XPATH = "//div[@id='state']//div[@tabindex and .='%s']";
    public static final String CITY_DROP_DOWN_ID = "city";
    public static final String CITY_VALUE_SUGGESTIONS_DOWN_BY_NAME_XPATH = "//div[@id='city']//div[@tabindex and .='%s']";
    public static final String SUBMIT_BUTTON_ID = "submit";
}
