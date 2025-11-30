package com.flamingo.qa.storefront.page_block_elements;

public class StudentRegistrationFormModalElements {

    public static final String FORM_POP_UP_BLOCK_XPATH = "//div[@class='modal-content' and div/div[.='Thanks for submitting the form']]";
    private static final String FORM_TABLE_BLOCK_XPATH = FORM_POP_UP_BLOCK_XPATH + "/div[@class='modal-body']//tbody";
    public static final String NAME_FIELD_XPATH = FORM_TABLE_BLOCK_XPATH + "//tr[td[.='Student Name']]/td[2]";
    public static final String EMAIL_FIELD_XPATH = FORM_TABLE_BLOCK_XPATH + "//tr[td[.='Student Email']]/td[2]";
    public static final String GENDER_FIELD_XPATH = FORM_TABLE_BLOCK_XPATH + "//tr[td[.='Gender']]/td[2]";
    public static final String MOBILE_FIELD_XPATH = FORM_TABLE_BLOCK_XPATH + "//tr[td[.='Mobile']]/td[2]";
    public static final String DATE_OF_BIRTH_FIELD_XPATH = FORM_TABLE_BLOCK_XPATH + "//tr[td[.='Date of Birth']]/td[2]";
    public static final String SUBJECTS_FIELD_XPATH = FORM_TABLE_BLOCK_XPATH + "//tr[td[.='Subjects']]/td[2]";
    public static final String HOBBIES_FIELD_XPATH = FORM_TABLE_BLOCK_XPATH + "//tr[td[.='Hobbies']]/td[2]";
    public static final String PICTURE_FIELD_XPATH = FORM_TABLE_BLOCK_XPATH + "//tr[td[.='Picture']]/td[2]";
    public static final String ADDRESS_FIELD_XPATH = FORM_TABLE_BLOCK_XPATH + "//tr[td[.='Address']]/td[2]";
    public static final String STATE_AND_CITY_FIELD_XPATH = FORM_TABLE_BLOCK_XPATH + "//tr[td[.='State and City']]/td[2]";
    public static final String CLOSE_BUTTON_ID = "closeLargeModal";

}
