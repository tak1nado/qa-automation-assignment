package com.flamingo.qa.helpers.models.student;

public enum City {
    DELHI(State.NCR, "Delhi"),
    AGRA(State.UTTAR_PRADESH, "Agra"),
    KARNAL(State.HARYANA, "Karnal"),
    JAISELMER(State.RAJASTHAN, "Jaiselmer");


    public final State state;
    public final String genderText;

    City(State state, String genderText) {
        this.state = state;
        this.genderText = genderText;
    }
}
