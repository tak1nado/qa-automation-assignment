package com.flamingo.qa.helpers.models.bookings;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@Builder
public class BookingData {
    private String firstname;
    private String lastname;
    private Double totalPrice;
    private Boolean isDepositPaid;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private String additionalNeeds;

    public String getCheckInDateString() {
        return convertToZonedDateString(this.checkIn);
    }

    public String getCheckOutDateString() {
        return convertToZonedDateString(this.checkOut);
    }

    private String convertToZonedDateString(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
