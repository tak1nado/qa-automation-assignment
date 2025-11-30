package com.flamingo.qa.helpers.models.student;

public enum Subject {
    ENGLISH("English"),
    MATHS("Maths"),
    ARTS("Arts");

    public String subjectText;

    Subject(String subjectText) {
        this.subjectText = subjectText;
    }
}
