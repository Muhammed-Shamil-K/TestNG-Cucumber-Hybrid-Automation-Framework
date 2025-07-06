package hybridAutomation.Pages.anywhereAction;

import hybridAutomation.Utilities.RetryUtil;
import hybridAutomation.Utilities.UIUtil;
import hybridAutomation.elements.Impl.TableImpl;
import hybridAutomation.elements.Table;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class Dashboard {

    private WebDriver driver;

    @FindBy(css = "div.menu-item")
    List<WebElement> menuItems;

    @FindBy(css = "div.sub-item")
    List<WebElement> subMenuItems;

    @FindBy(css = "div.dashboard")
    WebElement dashboard;

    @FindBy(css = "div.loader-container")
    WebElement loaderIcon;

    @FindBy(xpath = "//div[@class='table-container']/parent::div")
    WebElement table;

    @FindBy(css = "div.header-container img.menu-icon")
    WebElement menuIcon;

    @FindBy(css = "div.navigation-menu")
    WebElement navMenu;

    public Dashboard(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        RetryUtil.retry(() -> dashboard.isDisplayed(), 15);
    }


    public void selectMenuItem(String item) {
        UIUtil.waitUntilInvisibilityOf(driver, loaderIcon, 30);
        if(! navMenu.isDisplayed()) {
            menuIcon.click();
        }
        RetryUtil.retry(()-> {
            selectItemOnMenu(menuItems, item);
            return true;
        },10);
    }

    public void selectSubMenuItem(String item) {
        UIUtil.waitUntilInvisibilityOf(driver, loaderIcon, 30);
        if(! navMenu.isDisplayed()) {
            menuIcon.click();
        }
        RetryUtil.retry(()-> {
            selectItemOnMenu(subMenuItems, item);
            return true;
        },10);
    }

    public void editProperty(String propertyAddress) {
        RetryUtil.retry(()->table.isDisplayed(), 15);
        Table table = new TableImpl(driver.findElement(By.xpath("//div[@class='table-container']/parent::div")));
        table.editRow("Property Address", propertyAddress);
        UIUtil.waitUntilInvisibilityOf(driver, loaderIcon, 30);
    }

    public void tableOperation() {
        RetryUtil.retry(()->table.isDisplayed(), 15);
        Table table1 = new TableImpl(driver.findElement(By.xpath("//div[@class='table-container']/parent::div")));
        table1.getRowData(1);
        table1.getRowData("Auction Time", "03:30 AM");
        table1.getCellData(1, "Auction Time");
        table1.getCellData("Auction Time", "03:30 AM", "Auction Date");
        table1.filterTableData("Agent", "agent one");
        //table1.editRow("Auction Time", "03:30 AM");
    }

    public void tableOperation2() {
        RetryUtil.retry(()->table.isDisplayed(), 15);
        Table table1 = new TableImpl(driver.findElement(By.xpath("//div[@class='manage-container']")));
        table1.getRowData("Primary Agent", "primary agent");
        table1.filterTableData("Listing Status", "Draft");
        table1.search("AutoNum");
        table1.clickOnCell(1, "Create Public Access URL for EOIs");
        table1.clearSearch();
    }

    private void selectItemOnMenu(List<WebElement> dashboardItems, String matchingItem) {
        Optional<WebElement> menuItem = dashboardItems.stream().filter(menu-> menu.getText().contains(matchingItem)).findFirst();
        if (menuItem.isPresent()) {
            menuItem.get().click();
        }else {
            throw new NoSuchElementException("Item " + matchingItem + " is not available on Dashboard..");
        }
        UIUtil.waitUntilInvisibilityOf(driver, loaderIcon, 30);
    }

}
