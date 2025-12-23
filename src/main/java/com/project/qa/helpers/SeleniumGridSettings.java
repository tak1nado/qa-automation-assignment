package com.project.qa.helpers;

import lombok.Getter;

import java.net.MalformedURLException;
import java.net.URL;

public class SeleniumGridSettings {

    @Getter private URL hubUrl;

    public SeleniumGridSettings(String hubUrl) throws MalformedURLException {
        if (hubUrl != null && !hubUrl.isEmpty())
            this.hubUrl = new URL(hubUrl);
    }
}
