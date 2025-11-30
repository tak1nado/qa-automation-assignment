package com.flamingo.qa.helpers.models.student;

public enum Hobby {
    SPORTS("Sports"),
    READING("Reading"),
    MUSIC("Music");

    public String hobbyText;

    Hobby(String hobbyText) {
        this.hobbyText = hobbyText;
    }
}
