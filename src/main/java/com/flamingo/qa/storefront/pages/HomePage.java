package com.flamingo.qa.storefront.pages;

import com.flamingo.qa.storefront.StorefrontBasePage;
import org.springframework.stereotype.Component;

@Component
public class HomePage extends StorefrontBasePage {

    @Override
    public String getPageUrl() {
        return storefrontCockpit.getBaseUrl();
    }
}
