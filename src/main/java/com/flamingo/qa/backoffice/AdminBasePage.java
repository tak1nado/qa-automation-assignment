package com.flamingo.qa.backoffice;

import com.flamingo.qa.helpers.web.page.BasePageObject;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AdminBasePage extends BasePageObject {
    @Autowired protected AdminCockpit adminCockpit;

    public abstract String getPageUrl();
}
