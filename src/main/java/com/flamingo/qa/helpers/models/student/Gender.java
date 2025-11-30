package com.flamingo.qa.helpers.models.student;

import lombok.Data;

public enum Gender {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other");

    public String genderText;

    Gender(String genderText) {
        this.genderText = genderText;
    }
}
