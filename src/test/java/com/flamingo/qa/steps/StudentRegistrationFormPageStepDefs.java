package com.flamingo.qa.steps;

import com.flamingo.qa.helpers.managers.student.StudentsManager;
import com.flamingo.qa.helpers.models.student.StudentData;
import com.flamingo.qa.storefront.pages.StudentRegistrationPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

public class StudentRegistrationFormPageStepDefs extends AbstractStepDefs {
    @Autowired private StudentRegistrationPage studentRegistrationPage;
    @Autowired private StudentsManager studentsManager;

    @Given("Open Student Registration Form page.")
    public void openStudentRegistrationFormPage() {
        studentRegistrationPage.open();
    }

    @When("Fill in Student Registration form on Student Registration Form page.")
    public void fillInStudentRegistrationFormOnStudentRegistrationFormPage() {
        StudentData studentData = threadVarsHashMap.get(TestKeyword.STUDENT_DATA);
        studentRegistrationPage.fillInFirstNameField(studentData.getFirstname());
        studentRegistrationPage.fillInLastNameField(studentData.getLastname());
        studentRegistrationPage.selectGenderRadiobutton(studentData.getGender());
        studentRegistrationPage.fillInPhoneNumberField(studentData.getPhoneNumber());
        studentRegistrationPage.selectDateOfBirth(studentData.getBirthDay());
        studentRegistrationPage.selectSubjects(studentData.getSubjects());
        studentRegistrationPage.uploadPicture(studentData.getPicture());
        studentRegistrationPage.fillInAddress(studentData.getAddress());
        studentRegistrationPage.selectState(studentData.getState());
        studentRegistrationPage.selectCity(studentData.getCity());
    }

    @And("Click on Submit button on Student Registration Form page.")
    public void clickOnSubmitButtonOnStudentRegistrationFormPage() {
        studentRegistrationPage.clickOnSubmitButton();
    }
}
