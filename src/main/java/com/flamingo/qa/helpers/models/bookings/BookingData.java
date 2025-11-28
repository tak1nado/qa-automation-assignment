package com.flamingo.qa.helpers.models.bookings;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

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

}
