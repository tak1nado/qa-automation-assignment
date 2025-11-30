package com.flamingo.qa.steps;

import com.flamingo.qa.helpers.managers.student.StudentsManager;
import com.flamingo.qa.helpers.models.student.StudentData;
import com.flamingo.qa.storefront.page_blocks.StudentRegistrationFormModal;
import com.flamingo.qa.storefront.pages.StudentRegistrationPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.SoftAssertions;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class StudentRegistrationFormPageStepDefs extends AbstractStepDefs {
    @Autowired private StudentRegistrationPage studentRegistrationPage;
    @Autowired private StudentRegistrationFormModal studentRegistrationFormModal;
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
        studentRegistrationPage.selectHobbiesCheckbox(studentData.getHobbies());
        studentRegistrationPage.uploadPicture(studentData.getPicture());
        studentRegistrationPage.fillInAddress(studentData.getAddress());
        studentRegistrationPage.selectState(studentData.getState());
        studentRegistrationPage.selectCity(studentData.getCity());
    }

    @And("Click on Submit button on Student Registration Form page.")
    public void clickOnSubmitButtonOnStudentRegistrationFormPage() {
        studentRegistrationPage.clickOnSubmitButton();
        //TODO create student instance to track it in framework and delete after tests
    }

    @Then("Check that Student Registration submission pop-up is opened.")
    public void checkThatStudentRegistrationSubmissionPopUpIsOpened() {
        assertThat(studentRegistrationFormModal.isOpened())
                .as("Student Submission pop-up should be shown")
                .isTrue();
    }

    @And("Check that student data is correct in Student Registration submission pop-up.")
    public void checkThatStudentDataIsCorrectInStudentRegistrationSubmissionPopUp() {
        StudentData studentData = threadVarsHashMap.get(TestKeyword.STUDENT_DATA);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(studentRegistrationFormModal.getFirstName())
                .as("Firstname data should be equal.")
                .isEqualTo(studentData.getFirstname());
        softly.assertThat(studentRegistrationFormModal.getLastName())
                .as("Lastname data should be equal.")
                .isEqualTo(studentData.getLastname());
        softly.assertThat(studentRegistrationFormModal.getEmailField())
                .as("Email data should be equal.")
                .isEqualTo(studentData.getEmail());
        softly.assertThat(studentRegistrationFormModal.getGender())
                .as("Gender data should be equal.")
                .isEqualTo(studentData.getGender().genderText);
        softly.assertThat(studentRegistrationFormModal.getPhoneNumber())
                .as("Phone number data should be equal.")
                .isEqualTo(studentData.getPhoneNumber());
        softly.assertThat(studentRegistrationFormModal.getDateOfBirth())
                .as("Date od birth data should be equal.")
                .isEqualTo(studentData.getBirthDayString());
        softly.assertThat(studentRegistrationFormModal.getSubjects())
                .as("Subjects data should be equal.")
                .isEqualTo(studentData.getSubjects());
        softly.assertThat(studentRegistrationFormModal.getHobbies())
                .as("Hobbies data should be equal.")
                .isEqualTo(studentData.getHobbies());
        softly.assertThat(studentRegistrationFormModal.getAddress())
                .as("Address data should be equal.")
                .isEqualTo(studentData.getAddress());
        softly.assertThat(studentRegistrationFormModal.getCity())
                .as("City data should be equal.")
                .isEqualTo(studentData.getCity().cityText);
        softly.assertThat(studentRegistrationFormModal.getState())
                .as("State data should be equal.")
                .isEqualTo(studentData.getState().stateText);
        softly.assertAll();

    }
}
