package hybridAutomation.Pages.anywhereAction;

import hybridAutomation.Utilities.RetryUtil;
import hybridAutomation.Utilities.UIUtil;
import hybridAutomation.elements.DatePicker;
import hybridAutomation.elements.Impl.DatePickerImpl;
import hybridAutomation.elements.Impl.ModalImpl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class Property {

    private WebDriver driver;
    public Property(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "input[formcontrolname='street_number']")
    private WebElement streetNumber;

    @FindBy(css = "input[formcontrolname='street_name']")
    private WebElement streetName;

    @FindBy(css = "input[formcontrolname='date']")
    private DatePicker datePicker;

    @FindBy(css = "select[formcontrolname= 'hour']")
    private WebElement hourSelection;

    @FindBy(css = "select[formcontrolname= 'minute']")
    private WebElement minuteSelection;

    @FindBy(css = "select[formcontrolname= 'period']")
    private WebElement periodSelection;

    @FindBy(css = "select[formcontrolname= 'primary_agent_id']")
    private WebElement agentSelection;

    @FindBy(css = "select[formcontrolname= 'state_id']")
    private WebElement stateSelection;

    @FindBy(css = "input[placeholder='Enter Suburb']")
    private WebElement suburb;

    @FindBy(css = "md-option")
    private List<WebElement> inputSuggestions;

    @FindBy(css = "input[formcontrolname='listing_price']")
    private WebElement price;

    @FindBy(css = "div.loader-container")
    WebElement loaderIcon;

    @FindBy(css = "div.doc-thumb img")
    WebElement documentThumbnail;

    @FindBy(css = "button.tag-btn")
    WebElement documentTagButton;

    @FindBy(css = "div.submit-button-group button")
    private List<WebElement> buttons;

    public void fillStreetNumber(String number) {
        streetNumber.sendKeys(number);
    }

    public void fillStreetName(String name) {
        streetName.sendKeys(name);
    }

    public void selectDate(int day) {
        new DatePickerImpl(driver.findElement(By.cssSelector("input[formcontrolname='date']"))).select(day);
    }

    public void selectTime(String time) {
        String hour = time.split(":")[0];
        String minute = time.split(":")[1].split(" ")[0];
        String period = time.split(":")[1].split(" ")[1];
        new Select(hourSelection).selectByVisibleText(hour);
        new Select(minuteSelection).selectByVisibleText(minute);
        new Select(periodSelection).selectByVisibleText(period);
    }

    public void selectAgent(String agentId) {
        new Select(agentSelection).selectByVisibleText(agentId);
    }

    public void selectState(String stateId) {
        new Select(stateSelection).selectByVisibleText(stateId);
    }

    public void selectSuburb(String suburbName) {
        suburb.sendKeys(suburbName);
        Optional<WebElement> suburbItem = inputSuggestions.stream().filter(item-> item.getText().contains(suburbName))
                .findFirst();
        if (suburbItem.isPresent()) {
            suburbItem.get().click();
        } else {
            throw new NoSuchElementException("Suburb " + suburbName + " is not available in suggestion or its invalid");
        }
    }

    public void fillAuctionSetupForm(String streetNumber, String streetName, int day, String time, String agent,
                                     String state, String suburb) {
        fillStreetNumber(streetNumber);
        selectDate(day);
        selectTime(time);
        fillStreetName(streetName);
        selectAgent(agent);
        selectState(state);
        selectSuburb(suburb);
    }

    public void addPropertyPrice(String priceValue) {
        price.sendKeys(priceValue);
    }

    public void uploadLegalDocument(String documentPath) {
        WebElement legalDocuments = driver.findElements(By.cssSelector("div.mdl-cell.mdl-cell--6-col.mdl-cell--8-col-tablet")).stream()
                .filter(element -> element.getText().contains("UPLOAD CONTRACT OF SALE FOR E-SIGNING")).findFirst().get();
        WebElement browse = legalDocuments.findElement(By.cssSelector("input#contract-document"));
        browse.sendKeys(documentPath);
        UIUtil.waitUntilInvisibilityOf(driver, loaderIcon, 30);
        RetryUtil.retry(()-> documentThumbnail.isDisplayed(), 15);
    }

    public void tagDocument() {
        UIUtil.scrollByElement(driver, documentTagButton);
        documentTagButton.click();
        new ModalImpl(driver.findElement(By.cssSelector("md-dialog-container"))).clickButton("Continue");
    }

    public void savePropertyToDraft() {
        clickButton("SAVE TO DRAFT");
        UIUtil.waitUntilInvisibilityOf(driver, loaderIcon, 30);
    }

    public void saveAndPreviewProperty() {
        clickButton("SAVE AND PREVIEW");
    }

    public void publishProperty() {
        clickButton("PUBLISH");
    }

    public void exit() {
        RetryUtil.retry(()-> {
            clickButton("EXIT");
            return true;
        }, 10);
        exitModalDialog();
    }

    private void clickButton(String buttonText) {
        Optional<WebElement> matchedButton = buttons.stream().filter(button-> button.getText().equals(buttonText)).findFirst();
        if (matchedButton.isPresent()) {
            UIUtil.waitUntilElementIsClickable(driver, matchedButton.get(), 10);
            matchedButton.get().click();
        } else {
            throw new NoSuchElementException("Button with text " + buttonText + " is not present..");
        }
        UIUtil.waitUntilInvisibilityOf(driver, loaderIcon, 30);
    }

    public void exitModalDialog() {
        RetryUtil.retry(()-> {
            new ModalImpl(driver.findElement(By.cssSelector("md-dialog-container"))).clickButton("EXIT");
            return true;
        }, 10);

    }

    public void acceptModalDialog() {
        RetryUtil.retry(()-> {
            new ModalImpl(driver.findElement(By.cssSelector("md-dialog-container"))).clickButton("OK");
            return true;
        }, 10);
    }

    public void confirmModalDialog() {
        RetryUtil.retry(()-> {
            new ModalImpl(driver.findElement(By.cssSelector("md-dialog-container"))).clickButton("Confirm");
            return true;
        }, 10);
    }
}
