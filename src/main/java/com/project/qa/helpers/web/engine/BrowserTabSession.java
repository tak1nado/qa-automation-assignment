package com.project.qa.helpers.web.engine;

import com.microsoft.playwright.Page;
import lombok.Data;

@Data
public class BrowserTabSession {
    private final Page page;
    private boolean active = false;

    public BrowserTabSession(Page page) {
        this.page = page;
        this.active = true;
    }

}
