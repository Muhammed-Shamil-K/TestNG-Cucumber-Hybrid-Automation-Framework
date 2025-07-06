package hybridAutomation.elements.Impl;

import hybridAutomation.Utilities.RetryUtil;
import hybridAutomation.Utilities.UIUtil;
import hybridAutomation.elements.Table;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import java.util.*;

public class TableImpl implements Table {

    private final WebElement element;
    private final WebDriver driver;

    private List<String> headers;
    private List<WebElement> rows;

    public TableImpl(WebElement element) {
        this.driver = ((RemoteWebElement) element).getWrappedDriver();
        this.element = element;
        initTableElements();
    }

    @Override
    public void initTableElements() {
        RetryUtil.retry(()-> element.isDisplayed(), 15);
        this.headers = element.findElements(By.cssSelector("div.heading-row div.t-head")).stream()
                .map(WebElement::getText).toList();
        this.rows = element.findElements(By.cssSelector("div.row"));
    }

    @Override
    public Map<String, String> getRowData(int rowNumber) {
        Map<String, String> data = new HashMap<>();
        List<String> tableRowElements = rows.get(rowNumber - 1).findElements(By.cssSelector("div.t-row")).stream()
                .map(WebElement::getText).toList();
        for (int i = 0; i < headers.size(); i++) {
            data.put(headers.get(i), tableRowElements.get(i));
        }
        return data;
    }

    @Override
    public Map<String,String> getRowData(String columnName, String rowMatchPattern) {
        int columnIndex = headers.indexOf(columnName);
        WebElement matchedRow = getMatchedRow(rowMatchPattern, columnIndex);
        List<String> tableRowElements = getTableRowElements(matchedRow);
        Map<String, String> data = new HashMap<>();
        for (int i = 0; i < headers.size(); i++) {
            data.put(headers.get(i), tableRowElements.get(i));
        }
        return data;
    }

    @Override
    public String getCellData(int rowNumber, String columnMatchPattern) {
        WebElement matchedRow = rows.get(rowNumber - 1);
        int columnIndex = headers.indexOf(columnMatchPattern);
        List<String> tableRowElements = getTableRowElements(matchedRow);
        return tableRowElements.get(columnIndex);
    }

    @Override
    public String getCellData(String columnName, String rowMatchPattern, String columnMatchPattern) {
        int columnMatchIndex = headers.indexOf(columnName);
        int columnIndex = headers.indexOf(columnMatchPattern);
        WebElement matchedRow = getMatchedRow(rowMatchPattern, columnMatchIndex);
        List<String> tableRowElements = getTableRowElements(matchedRow);
        return tableRowElements.get(columnIndex);
    }

    @Override
    public void editRow(int rowNumber) {
        WebElement matchedRow = rows.get(rowNumber - 1);
        matchedRow.findElement(By.cssSelector("div.edit")).click();
    }

    @Override
    public void editRow(String columnName, String rowMatchPattern) {
        int columnIndex = headers.indexOf(columnName);
        WebElement matchedRow = getMatchedRow(rowMatchPattern, columnIndex);
        RetryUtil.retry(()-> {
            matchedRow.findElement(By.cssSelector("div.edit")).click();
            return true;
        }, 10);
    }

    @Override
    public void clickOnCell(int rowNumber, String columnMatchPattern) {
        RetryUtil.retry(()-> {
            WebElement matchedRow = rows.get(rowNumber - 1);
            int columnIndex = headers.indexOf(columnMatchPattern);
            matchedRow.findElements(By.cssSelector("div.t-row")).get(columnIndex).findElement(By.cssSelector("div")).click();
            return true;
        }, 10);
    }

    @Override
    public void clickOnCell(String columnName, String rowMatchPattern, String columnMatchPattern) {
        int columnMatchIndex = headers.indexOf(columnName);
        int columnIndex = headers.indexOf(columnMatchPattern);
        WebElement matchedRow = getMatchedRow(rowMatchPattern, columnMatchIndex);
        matchedRow.findElements(By.cssSelector("div.t-row")).get(columnIndex).findElement(By.cssSelector("div")).click();
    }

    @Override
    public void filterTableData(String filterLabel, String filterOption) {
        String xpath = "//div[text()='" + filterLabel +"']/following-sibling::div";
        WebElement filter = element.findElement(By.xpath(xpath));
        filter.findElement(By.cssSelector("div.dd-heading")).click();
        Optional<WebElement> matchedOption = filter.findElements(By.cssSelector("div.dd-content div.dd-item"))
                .stream().filter(item-> item.getText().equals(filterOption)).findFirst();
        if (matchedOption.isPresent()) {
            matchedOption.get().click();
        } else {
            throw new NoSuchElementException("The option " + filterOption + " is not available in the filter..");
        }
        UIUtil.waitUntilInvisibilityOf(driver, driver.findElement(By.cssSelector("div.loader-container")), 30);
        initTableElements();
    }

    @Override
    public void search(String input) {
        element.findElement(By.cssSelector("input[name='search']")).sendKeys(input);
        element.findElement(By.cssSelector("button.search-input__submit")).click();
        UIUtil.waitUntilInvisibilityOf(driver, driver.findElement(By.cssSelector("div.loader-container")), 30);
        initTableElements();
    }

    @Override
    public void clearSearch() {
        element.findElement(By.cssSelector("button.search-input__reset")).click();
        UIUtil.waitUntilInvisibilityOf(driver, driver.findElement(By.cssSelector("div.loader-container")), 30);
        initTableElements();
    }

    private WebElement getMatchedRow(String rowMatchPatter, int columnIndex) {
        WebElement matchedRow = null;
        for(WebElement row : rows) {
            List<String> rowCells = getTableRowElements(row);
            if(rowCells.get(columnIndex).equalsIgnoreCase(rowMatchPatter)) {
                matchedRow = row;
                break;
            }
        }
        if (matchedRow != null) {
            return matchedRow;
        } else {
            WebElement nextButton = element.findElement(By.cssSelector("li.pagination-next"));
            if(nextButton.isEnabled()) {
                nextButton.click();
                UIUtil.waitUntilInvisibilityOf(driver, driver.findElement(By.cssSelector("div.loader-container")), 30);
                UIUtil.waitUntilDisplayed(element, 15);
                initTableElements();
                return getMatchedRow(rowMatchPatter, columnIndex);
            } else {
                throw new NoSuchElementException("Could not find a matched row with the row matcher " + rowMatchPatter);
            }
        }
    }

    private List<String> getTableRowElements(WebElement row) {
        return RetryUtil.retry(()-> row.findElements(By.cssSelector("div.t-row")).stream()
                .map(WebElement::getText).toList(), 10);
    }
}
