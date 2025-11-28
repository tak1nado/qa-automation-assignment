package com.flamingo.qa.storefront;

import com.flamingo.qa.helpers.web.page.BasePageObject;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class StorefrontBasePage extends BasePageObject {
    @Autowired protected StorefrontCockpit storefrontCockpit;

    public abstract String getPageUrl();
}
