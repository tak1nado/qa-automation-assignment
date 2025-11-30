package com.flamingo.qa.steps;

import com.flamingo.qa.api.controller.actions.BookingActions;
import com.flamingo.qa.helpers.models.bookings.Booking;
import io.cucumber.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;

public class DataGeneratorStepDefs extends AbstractStepDefs {
    @Autowired private BookingActions bookingActions;

    @And("^Booking is created\\.$")
    public void checkThatCreatedBookingDataIsCorrectInTheTable() {
        Booking booking = bookingActions.getCreatedBooking();
        threadVarsHashMap.put(TestKeyword.BOOKING, booking);
    }

    @And("Generate Student data based on created booking.")
    public void generateStudentDataBasedOnCreatedBooking() {
        Booking booking = threadVarsHashMap.get(TestKeyword.BOOKING);
    }
}
