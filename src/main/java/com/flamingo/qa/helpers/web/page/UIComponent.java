package com.flamingo.qa.helpers.web.page;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flamingo.qa.helpers.web.engine.WebDriverSessions;
import io.qameta.allure.Step;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Log
public abstract class UIComponent {

    @Autowired protected WebDriverSessions webDriverPool;

    protected WebDriver getDriver() {
        return webDriverPool.getActiveDriver();
    }

    @Step("Accept alert.")
    protected void alertHandling() {
        getDriver().switchTo().alert().accept();
    }

    protected WebElement $(String xpath, String... args) {
        try {
            return getDriver().findElement(By.xpath(String.format(xpath, args)));
        } catch (UnhandledAlertException ex) {
            log.warning("WARNING: Unexpected alert: " + ex);
            alertHandling();
            return $(xpath, args);
        }
    }

    protected WebElement $(By by) {
        try {
            return getDriver().findElement(by);
        } catch (UnhandledAlertException ex) {
            log.warning("WARNING: Unexpected alert!");
            alertHandling();
            return $(by);
        }
    }

    protected List<WebElement> $$(String xpath, String... args) {
        return getDriver().findElements(By.xpath(String.format(xpath, args)));
    }

    protected List<String> getAttributes(String attribute, String xpath, String... args) {
        return getDriver().findElements(By.xpath(String.format(xpath, args))).stream()
                .map(webElement -> webElement.getAttribute(attribute))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    protected String getAttribute(String attribute, String xpath, String... args) {
        return getDriver().findElement(By.xpath(String.format(xpath, args))).getAttribute(attribute);
    }

    protected List<WebElement> getElementsInFrame(String frameName, String xpath, String... args) {
        getDriver().switchTo().frame(frameName);
        List<WebElement> webElements = $$(xpath, args);
        getDriver().switchTo().defaultContent();
        return webElements;
    }

    protected List<WebElement> $$(By by) {
        return getDriver().findElements(by);
    }

    protected void clickInFrame(String frameName, String xpath, String... args) {
        getDriver().switchTo().frame(frameName);
        click(xpath, args);
        getDriver().switchTo().defaultContent();
    }

    protected void clickInFrame(String frameName, By by) {
        getDriver().switchTo().frame(frameName);
        click(by);
        getDriver().switchTo().defaultContent();
    }

    protected boolean isSelectedInFrame(String frameName, String xpath, String... args) {
        getDriver().switchTo().frame(frameName);
        boolean result = $(xpath, args).isSelected();
        getDriver().switchTo().defaultContent();
        return result;
    }

    protected void click(String xpath, String... args) {
        waitUntilElementIsVisible(xpath, args);
        WebElement webElement = $(xpath, args);
        webDriverPool.getActiveDriverSession().setShortImplicitWait();
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofMillis(webDriverPool.getActiveDriverSession().getShortTimeOut()));
            wait.until(driver -> isElementClickable(webElement));
                webElement.click();
        } catch (StaleElementReferenceException exception) {
            log.warning("ERROR: Page stale element exception. Retry. " + exception);
            click(xpath, args);
        } catch (WebDriverException | NullPointerException ex) {
            log.warning("ERROR: Element exception found: " + ex);
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofMillis(webDriverPool.getActiveDriverSession().getMediumTimeOut()));
            wait.until(driver -> clickPerformed(webElement));
        } finally {
            webDriverPool.getActiveDriverSession().restoreDefaultImplicitWait();
        }
    }

    protected void click(By by) {
        waitUntilElementIsVisible(by);
        WebElement webElement = $(by);
        webDriverPool.getActiveDriverSession().setShortImplicitWait();
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofMillis(webDriverPool.getActiveDriverSession().getShortTimeOut()));
            wait.until(driver -> isElementClickable(webElement));
            webElement.click();
        } catch (StaleElementReferenceException exception) {
            log.warning("ERROR: Page stale element exception. Retry. " + exception);
            click(by);
        } catch (WebDriverException | NullPointerException ex) {
            log.warning("ERROR: Element exception found: " + ex);
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofMillis(webDriverPool.getActiveDriverSession().getMediumTimeOut()));
            wait.until(driver -> clickPerformed(webElement));
        } finally {
            webDriverPool.getActiveDriverSession().restoreDefaultImplicitWait();
        }
    }

    protected void clickEvery(String xpath, String... args) {
        List<WebElement> elements = $$(xpath, args);
        for(WebElement element : elements) {
            element.click();
            sleep(300);
        }
    }

    private boolean clickPerformed(WebElement webElement) {
        try {
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView({block: 'center',});", webElement);
            webElement.click();
            return true;
        } catch (ElementNotInteractableException ex) {
            return false;
        }
    }

    private boolean isElementClickable(WebElement webElement) {
        return webElement != null && webElement.isDisplayed() && webElement.isEnabled();
    }

    public boolean isElementClickable(String xpath, String... args) {
        WebElement webElement = $(xpath, args);
        return webElement != null && webElement.isDisplayed() && webElement.isEnabled();
    }

    private boolean isElementClickable(By by) {
        WebElement webElement = $(by);
        return webElement != null && webElement.isDisplayed() && webElement.isEnabled();
    }

    protected void enterTextAndBlur(String text, By by) {
        if (text == null)
            text = StringUtils.EMPTY;
        waitUntilElementIsVisible(by);
        $(by).clear();
        $(by).sendKeys(text);
        blurElement($(by));
    }

    protected void enterTextAndBlur(String text, String xpath, String... args) {
        if (text == null)
            text = StringUtils.EMPTY;
        waitUntilElementIsVisible(xpath, args);
        $(xpath, args).clear();
        $(xpath, args).sendKeys(text);
        blurElement($(xpath, args));
    }

    protected void enterText(String text, By by) {
        if (text == null)
            text = StringUtils.EMPTY;
        waitUntilElementIsVisible(by);
        $(by).clear();
        $(by).sendKeys(text);
    }

    //Without clearing before entering the text
    protected void sendKeys(String text, By by) {
        if (text == null)
            text = StringUtils.EMPTY;
        waitUntilElementIsVisible(by);
        $(by).sendKeys(text);
    }

    protected void enterText(String text, String xpath, String... args) {
        if (text == null)
            text = StringUtils.EMPTY;
        waitUntilElementIsVisible(xpath, args);
        $(xpath, args).clear();
        $(xpath, args).sendKeys(text);
    }

    //Without clearing before entering the text
    protected void sendKeys(String text, String xpath, String... args) {
        if (text == null)
            text = StringUtils.EMPTY;
        waitUntilElementIsVisible(xpath, args);
        $(xpath, args).sendKeys(text);
    }

    protected void clearTextViaBackspace(String xpath, String... args) {
        String selectedState = getAttribute("value", xpath, args);
        pressBackspace(selectedState.length(), xpath, args);
    }

    protected void pressBackspace(int numberOfClicks, String xpath, String... args) {
        for (int i = 0; i < numberOfClicks; i++)
            $(xpath, args).sendKeys("\u0008");
    }

    protected void pressEnter() {
        new Actions(getDriver()).sendKeys(Keys.ENTER).perform();
        sleep(300);
    }

    protected void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void fillInTextCharByChar(String text, By by) {
        for(int i = 0; i < text.length(); i++){
            $(by).sendKeys(String.valueOf(text.charAt(i)));
        }
    }

    protected void fillInTextCharByChar(String text, String xpath, String... args) {
        for(int i = 0; i < text.length(); i++){
            $(xpath, args).sendKeys(String.valueOf(text.charAt(i)));
            sleep(50);
        }
    }

    protected void enterTextCharByChar(String text, By by) {
        if (text == null)
            text = StringUtils.EMPTY;
        waitUntilElementIsVisible(by);
        $(by).clear();
        fillInTextCharByChar(text, by);
    }

    protected void enterTextCharByChar(String text, String xpath, String... args) {
        if (text == null)
            text = StringUtils.EMPTY;
        waitUntilElementIsVisible(xpath, args);
        $(xpath, args).clear();
        fillInTextCharByChar(text, xpath, args);
    }

    protected void fillInTextCharByCharAndBlur(String text, By by) {
        for(int i = 0; i < text.length(); i++){
            $(by).sendKeys(String.valueOf(text.charAt(i)));
        }
        blurElement($(by));
    }

    protected void fillInTextCharByCharAndBlur(String text, String xpath, String... args) {
        for(int i = 0; i < text.length(); i++){
            $(xpath, args).sendKeys(String.valueOf(text.charAt(i)));
            sleep(50);
        }
        blurElement($(xpath, args));
    }

    protected void enterTextCharByCharAndBlur(String text, By by) {
        if (text == null)
            text = StringUtils.EMPTY;
        waitUntilElementIsVisible(by);
        $(by).clear();
        fillInTextCharByCharAndBlur(text, by);
    }

    protected void enterTextCharByCharAndBlur(String text, String xpath, String... args) {
        if (text == null)
            text = StringUtils.EMPTY;
        waitUntilElementIsVisible(xpath, args);
        $(xpath, args).clear();
        fillInTextCharByCharAndBlur(text, xpath, args);
    }

    protected void clearText(String xpath, String... args) {
        waitUntilElementIsVisible(xpath, args);
        $(xpath, args).clear();
        blurElement($(xpath, args));
    }

    protected void clearText(By by) {
        waitUntilElementIsVisible(by);
        $(by).clear();
        blurElement($(by));
    }
    protected void clearAllText(String xpath) {
        new Actions(getDriver()).sendKeys($(xpath), Keys.CONTROL, Keys.BACK_SPACE).perform();
    }

    protected void blurElement(WebElement webElement) {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("arguments[0].focus(); arguments[0].blur(); return true", webElement);
    }

    protected void blurElement(String xpath, String... args) {
        WebElement webElement = $(xpath, args);
        blurElement(webElement);
    }

    protected void moveCursor(String xpath, String... args) {
        Actions actions = new Actions(getDriver());
        WebElement target = $(xpath, args);
        actions.moveToElement(target).perform();
    }

    protected boolean isDisplayedInFrame(String frameName, String xpath, String... args) {
        webDriverPool.getActiveDriverSession().setShortImplicitWait();
        try {
            getDriver().switchTo().frame(frameName);
        } catch (NoSuchFrameException ex) {
            log.warning(ex.getMessage());
            return false;
        } finally {
            webDriverPool.getActiveDriverSession().restoreDefaultImplicitWait();
        }
        boolean result = isDisplayed(xpath, args);
        getDriver().switchTo().defaultContent();
        return result;
    }

    protected boolean isDisplayed(String xpath, String... args) {
        webDriverPool.getActiveDriverSession().setShortImplicitWait();
        try {
            return $(xpath, args).isDisplayed();
        } catch (NoSuchElementException | NullPointerException ex) {
            return false;
        } finally {
            webDriverPool.getActiveDriverSession().restoreDefaultImplicitWait();
        }
    }

    protected boolean isDisplayed(By by) {
        webDriverPool.getActiveDriverSession().setShortImplicitWait();
        try {
            return $(by).isDisplayed();
        } catch (NoSuchElementException | NullPointerException ex) {
            return false;
        } finally {
            webDriverPool.getActiveDriverSession().restoreDefaultImplicitWait();
        }
    }

    protected boolean isPresent(String xpath, String... args) {
        webDriverPool.getActiveDriverSession().setShortImplicitWait();
        try {
            getDriver().findElement(By.xpath(String.format(xpath, args)));
            return true;
        } catch (UnhandledAlertException ex) {
            log.warning("WARNING: Unexpected alert: " + ex);
            alertHandling();
            return isPresent(xpath, args);
        } catch (NoSuchElementException ex) {
            return false;
        } finally {
            webDriverPool.getActiveDriverSession().restoreDefaultImplicitWait();
        }
    }

    protected Wait<WebDriver> fluentWait() {
       return setFluentWait(webDriverPool.getActiveDriverSession().getMediumTimeOut());
    }

    protected boolean isPresent(By by) {
        webDriverPool.getActiveDriverSession().setShortImplicitWait();
        try {
            getDriver().findElement(by);
            return true;
        } catch (UnhandledAlertException ex) {
            log.warning("WARNING: Unexpected alert: " + ex);
            alertHandling();
            return isPresent(by);
        } catch (NoSuchElementException ex) {
            return false;
        } finally {
            webDriverPool.getActiveDriverSession().restoreDefaultImplicitWait();
        }
    }

    @Step("Wait until page is fully loaded.")
    protected void waitUntilPageIsFullyLoaded() {
        waitHTMLTemplateLoad();
//        waitElementLoadSpinnerIsNotDisplayed();
        confirmThatPageIsNotBlank();
    }

    //workaround to refresh page if it is blank from first try
    @Step("Confirm that page is not blank.")
    private void confirmThatPageIsNotBlank() {
        webDriverPool.getActiveDriverSession().setMediumImplicitWait();
        try {
            List<WebElement> children = getDriver().findElement(By.id("app")).findElements(By.xpath("/*"));
            if (children.isEmpty()) {
                refresh();
                waitHTMLTemplateLoad();
                waitElementLoadSpinnerIsNotDisplayed();
            }
        } catch (NoSuchElementException ex) {
            refresh();
        } catch (StaleElementReferenceException e) {
            log.info("Stale element exception. Continue...");
        } finally {
            webDriverPool.getActiveDriverSession().restoreDefaultImplicitWait();
        }
    }

    @Step("Refresh page.")
    private void refresh() {
        getDriver().navigate().refresh();
    }

    public void waitElementAttributeIsNotEmpty(String xpath, String attributeName) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofMillis(webDriverPool.getActiveDriverSession().getTimeOut()));
        wait.until(driver1 -> (!$(xpath).getAttribute(attributeName).isEmpty()));
    }

    @Step("Wait until HTML template is fully loaded.")
    public void waitHTMLTemplateLoad() {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofMillis(webDriverPool.getActiveDriverSession().getTimeOut()));
        wait.until(driver1 -> ((JavascriptExecutor) driver1).executeScript("return document.readyState").toString().equals("complete"));
    }

    @Step("Wait until element is not visible {0}.")
    public void waitUntilElementIsNotVisible(String xpath, String... args) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofMillis(webDriverPool.getActiveDriverSession().getShortTimeOut()));
        try {
            wait.until(driver1 -> !isDisplayed(xpath, args));
        } catch (StaleElementReferenceException ex) {
            waitUntilElementIsNotVisible(xpath, args);
        } catch (TimeoutException e) {
            log.warning("ERROR: Element still present after: " + wait.toString() + " period.");
        }
    }

    @Step("Wait until element is not visible {0}.")
    public void waitUntilElementIsNotVisible(By by) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofMillis(webDriverPool.getActiveDriverSession().getShortTimeOut()));
        try {
            wait.until(driver1 -> !isDisplayed(by));
        } catch (StaleElementReferenceException ex) {
            waitUntilElementIsNotVisible(by);
        }
    }

    @Step("Wait until element is visible {0}.")
    public void waitUntilElementIsVisible(String xpath, String... args) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofMillis(webDriverPool.getActiveDriverSession().getTimeOut()));
        wait.until(driver1 -> isDisplayed(xpath, args));
    }

    @Step("Wait until element is visible {0}.")
    public void softWaitUntilElementIsVisible(String xpath, String... args) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofMillis(webDriverPool.getActiveDriverSession().getShortTimeOut()));
        try {
            wait.until(driver1 -> isDisplayed(xpath, args));
        } catch (TimeoutException ex) {
            log.warning("[WARNING]: Element is not visible yet " + String.format(xpath, args));
        }
    }

    @Step("Wait until element is visible {0}.")
    public void waitUntilElementIsVisible(By by) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofMillis(webDriverPool.getActiveDriverSession().getTimeOut()));
        wait.until(driver1 -> isDisplayed(by));
    }

    @Step("Wait until {0} GraphQL response is sent.")
    protected void waitUntilRequestHasResponse(String requestName) {
        WebDriverWait waitRequest = new WebDriverWait(getDriver(), Duration.ofMillis(webDriverPool.getActiveDriverSession().getShortTimeOut()));
        WebDriverWait waitResponse = new WebDriverWait(getDriver(), Duration.ofMillis(webDriverPool.getActiveDriverSession().getTimeOut()));

        final StringBuilder requestId = new StringBuilder();

        //Wait request is sent
        try {
            waitRequest.until(driver1 -> {
                List<LogEntry> entries = getDriver().manage().logs().get(LogType.PERFORMANCE).getAll();

                Optional<String> foundId = entries.stream()
                        .map(LogEntry::getMessage)
                        .map(this::tryParseJson)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .filter(root -> {
                            if (!"Network.requestWillBeSent".equals(root.path("message").path("method").asText())) {
                                return false;
                            }
                            JsonNode params = root.path("message").path("params");
                            JsonNode request = params.path("request");
                            return params.has("documentURL")
                                    && request.has("postData")
                                    && request.get("postData").asText().contains(requestName);
                        })
                        .map(root -> root.path("message").path("params").path("requestId").asText())
                        .findAny();

                if (foundId.isPresent()) {
                    requestId.append(foundId.get());
                    return true;
                }
                return false;
            });
        } catch (TimeoutException ex) {
            log.info("[INFO] Request hasn't been appeared " + getClass().getName() + ", " + requestName + ".");
            return;
        }

        //Wait request is loaded
        try {
            waitResponse.until(driver1 -> {
                List<LogEntry> entries = getDriver().manage().logs().get(LogType.PERFORMANCE).getAll();

                return entries.stream()
                        .map(LogEntry::getMessage)
                        .map(this::tryParseJson)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .anyMatch(root -> {
                            JsonNode message = root.path("message");
                            JsonNode params = message.path("params");
                            boolean isTargetRequest = params.has("requestId")
                                    && params.get("requestId").asText().equals(requestId.toString());

                            if (!isTargetRequest) {
                                return false;
                            }
                            return "Network.loadingFinished".equals(message.path("method").asText());
                        });
            });
        } catch (TimeoutException ex) {
            log.warning("[WARNING] Timeout exception after WAITING FOR " + getClass().getName() + ", " + requestName + " REQUEST BE SENT.");
        }
    }

    private Optional<JsonNode> tryParseJson(String jsonString) {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            return Optional.of(mapper.readTree(jsonString));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    protected void clearRequestsHistory() {
        getDriver().manage().logs().get(LogType.PERFORMANCE).getAll();
    }

    public void waitForAngularLoad() {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofMillis(webDriverPool.getActiveDriverSession().getTimeOut()));
        try {
            Thread.sleep(webDriverPool.getActiveDriverSession().getShortTimeOut() / 20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            wait.until(driver1 -> {
                boolean result;
                JavascriptExecutor js = (JavascriptExecutor) driver1;
                try {
                    result = (Boolean) js.executeScript("return angular.element(document).injector().get('$http').pendingRequests.length === 0");
                } catch (WebDriverException ex) {
                    result = true;
                    log.info("[INFO] No Angular.");
                }
                return result;
            });
        } catch (TimeoutException ex) {
            log.info("[WARNING] Timeout exception after " + webDriverPool.getActiveDriverSession().getTimeOut() + " Seconds. Possible problem - browser hovered(not responding)!");
        }
        try {
            Thread.sleep((long) (webDriverPool.getActiveDriverSession().getShortTimeOut() / 20));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Step("Wait until JQuery is fully loaded.")
    public void waitJQueryRequestsLoad() {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofMillis(webDriverPool.getActiveDriverSession().getTimeOut()));
        try {
            Thread.sleep(webDriverPool.getActiveDriverSession().getShortTimeOut() / 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            wait.until(driver1 -> {
                boolean result;
                JavascriptExecutor js = (JavascriptExecutor) driver1;
                try {
                    result = (Boolean) js.executeScript("return jQuery.active == 0");
                } catch (WebDriverException ex) {
                    result = true;
                    log.info("[INFO] No jQueries.");
                }
                return result;
            });
        } catch (TimeoutException ex) {
            log.warning("[WARNING] Timeout exception after " + webDriverPool.getActiveDriverSession().getTimeOut() + " Seconds. Possible problem - browser hovered(not responding)!");
        }
        try {
            Thread.sleep(webDriverPool.getActiveDriverSession().getShortTimeOut() / 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //workaround for projects with spinner load on the page
    @Step("Wait until Load spinner is loaded.")
    public void waitElementLoadSpinnerIsNotDisplayed() {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofMillis(webDriverPool.getActiveDriverSession().getTimeOut()));
        webDriverPool.getActiveDriverSession().setMinimumImplicitWait();
        wait.until(driver -> {
            try {
                return !$("//*[@aria-label='Loading']").isDisplayed();
            } catch (NoSuchElementException | NullPointerException ex) {
                return true;
            } catch (StaleElementReferenceException e) {
                return false;
            }
        });
        webDriverPool.getActiveDriverSession().restoreDefaultImplicitWait();
    }

    private Wait<WebDriver> setFluentWait(int timeout) {
        return new FluentWait<>(getDriver())
                .withTimeout(Duration.ofMillis(timeout))
                .pollingEvery(Duration.ofMillis(300))
                .ignoring(NoSuchElementException.class);
    }

    protected String getText(String xpath, String... args) {
        setFluentWait(webDriverPool.getActiveDriverSession().getShortTimeOut())
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath(String.format(xpath, args))));
        WebElement element = $(xpath, args);
        return element.getText();
    }

    protected List<String> getTexts(String xpath, String... args) {
        webDriverPool.getActiveDriverSession().setShortImplicitWait();
        try {
            return $$(xpath, args).stream().map(webElement -> webElement.getText().trim()).collect(Collectors.toList());
        } catch (StaleElementReferenceException ex) {
            return getTexts(xpath, args);
        } finally {
            webDriverPool.getActiveDriverSession().restoreDefaultImplicitWait();
        }
    }

    protected List<String> getTextsWaiting(String xpath, String... args) {
        webDriverPool.getActiveDriverSession().setShortImplicitWait();
        try {
            fluentWait().until(wait -> $$(xpath, args).stream().map(webElement -> webElement.getText().trim()).collect(Collectors.toList()).size() > 0);
            return $$(xpath, args).stream().map(webElement -> webElement.getText().trim()).collect(Collectors.toList());
        } catch (StaleElementReferenceException ex) {
            return getTexts(xpath, args);
        } finally {
            webDriverPool.getActiveDriverSession().restoreDefaultImplicitWait();
        }
    }

    protected String getTextNode(WebElement webElement) {
        String text = webElement.getText().trim();
        List<WebElement> children = webElement.findElements(By.xpath("./*"));
        for (WebElement child : children) {
            text = text.replace(child.getText(), "").trim();
        }
        return text;
    }

    protected List<String> getNodesTexts(String xpath, String... args) {
        List<String> texts = new ArrayList<>();
        try {
            List<WebElement> textsElements = $$(xpath, args);
            for (WebElement textElement : textsElements) {
                String text = textElement.getText().trim();
                webDriverPool.getActiveDriverSession().setMinimumImplicitWait();
                List<WebElement> children = textElement.findElements(By.xpath("./*"));
                webDriverPool.getActiveDriverSession().restoreDefaultImplicitWait();
                for (WebElement child : children) {
                    text = text.replace(child.getText(), "").trim();
                }
                texts.add(text);
            }
        } catch (StaleElementReferenceException ex) {
            return getNodesTexts(xpath, args);
        }
        return texts;
    }

    protected String getTextNode(String xpath, String... args) {
        String text = $(xpath, args).getText().trim();
        List<WebElement> children = $(xpath, args).findElements(By.xpath("./*"));
        for (WebElement child : children) {
            text = text.replace(child.getText(), "").trim();
        }
        return text;
    }

    public void deselectCheckbox(WebElement element) {
        WebElement checkbox = element.findElement(By.xpath("//input[@class='x-checkbox__input']"));
        if(checkbox.isSelected()) { element.click(); }
    }

    public void selectCheckbox(WebElement element) {
        WebElement checkbox = element.findElement(By.xpath("//input[@class='x-checkbox__input']"));
        if(!checkbox.isSelected()) { element.click(); }
    }
}