package com.flamingo.qa.steps;

import com.flamingo.qa.api.controller.actions.BookingActions;
import com.flamingo.qa.helpers.managers.student.StudentsManager;
import com.flamingo.qa.helpers.models.bookings.Booking;
import io.cucumber.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;

public class DataGeneratorStepDefs extends AbstractStepDefs {
    @Autowired private BookingActions bookingActions;
    @Autowired private StudentsManager studentsManager;

    @And("Generate Student data based on created booking.")
    public void generateStudentDataBasedOnCreatedBooking() {
        Booking booking = threadVarsHashMap.get(TestKeyword.BOOKING);
        threadVarsHashMap.put(TestKeyword.STUDENT_DATA, studentsManager.generateRandomValidStudentDataWithBookingData(booking));
    }
}
