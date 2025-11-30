package com.flamingo.qa.helpers.models.student;

public enum City {
    DELHI(State.NCR, "Delhi"),
    AGRA(State.UTTAR_PRADESH, "Agra"),
    KARNAL(State.HARYANA, "Karnal"),
    JAISELMER(State.RAJASTHAN, "Jaiselmer");


    public final State state;
    public final String cityText;

    City(State state, String cityText) {
        this.state = state;
        this.cityText = cityText;
    }
}
