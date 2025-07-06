package hybridAutomation.Pages.anywhereAction;

import hybridAutomation.Core.Tag;
import hybridAutomation.Utilities.RetryUtil;
import hybridAutomation.Utilities.UIUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.openqa.selenium.support.locators.RelativeLocator.with;

public class TagDocument {

    private WebDriver driver;

    @FindBy(css = "canvas.upper-canvas")
    private WebElement canvas;

    @FindBy(css = "div.loader-container")
    WebElement loaderIcon;

    @FindBy(css = "div.tag-group")
    private List<WebElement> tagMenus;

    @FindBy(css = "div.property-btn button")
    private List<WebElement> buttons;

    public TagDocument(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        RetryUtil.retry(() -> canvas.isDisplayed(), 15);
    }

    public void addTags(Tag... tags) {
        tagMenus.get(0).click();
        for(Tag tag : tags) {
            addTag(tagMenus.get(0), tag);
        }
    }

    public void addTags(String tagHeader, Tag... tags) {
        Optional<WebElement> tagMenu = tagMenus.stream().filter(element -> element.getText().equals(tagHeader)).findFirst();
        if (tagMenu.isPresent()) {
            tagMenu.get().click();
            for(Tag tag : tags) {
                addTag(tagMenu.get(), tag);
            }
        } else {
            throw new NoSuchElementException(tagHeader + " tag menu is not available or its invalid..");
        }
    }

    private void addTag(WebElement tagMenu, Tag tagItem) {
        Optional<WebElement> item = driver.findElements(with(By.cssSelector("div.tagging-div"))
                        .near(tagMenu, 1000)).stream()
                .filter(tag-> tag.getText().equals(tagItem.tagName)).findFirst();
        if (item.isPresent()) {
            item.get().click();
        } else {
            //logger
            throw new NoSuchElementException("Tag item " + tagItem.tagName + " is not present on the menu");
        }
        new Actions(driver).moveToElement(canvas, tagItem.xOffset, tagItem.yOffset).click().perform();
    }

    public void saveAndPreviewDocument() {
        clickButton("SAVE & PREVIEW");
    }

    public void cancelDocumentTagging() {
        clickButton("CANCEL");
    }

    public void saveDocumentAndExit() {
        clickButton("SAVE & EXIT");
    }

    private void clickButton(String buttonText) {
        Optional<WebElement> matchedButton = buttons.stream().filter(button-> button.getText().equals(buttonText)).findFirst();
        if (matchedButton.isPresent()) {
            matchedButton.get().click();
        } else {
            throw new NoSuchElementException("Button with text " + buttonText + " is not present..");
        }
        UIUtil.waitUntilInvisibilityOf(driver, loaderIcon, 30);
    }


}
