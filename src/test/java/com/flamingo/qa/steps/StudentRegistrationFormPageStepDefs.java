package com.flamingo.qa.steps;

import com.flamingo.qa.storefront.pages.StudentRegistrationPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;

public class StudentRegistrationFormPageStepDefs extends AbstractStepDefs {
    @Autowired private StudentRegistrationPage studentRegistrationPage;

    @Given("Open Student Registration Form page.")
    public void openStudentRegistrationFormPage() {
        studentRegistrationPage.open();
    }
}
