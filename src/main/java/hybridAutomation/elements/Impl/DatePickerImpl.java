package hybridAutomation.elements.Impl;

import hybridAutomation.elements.DatePicker;
import hybridAutomation.elements.TestElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

public class DatePickerImpl implements DatePicker {


    private final TestElement element;
    private final WebDriver driver;

    public DatePickerImpl(WebElement element) {
        this.driver = ((RemoteWebElement) element).getWrappedDriver();
        this.element = new TestElementImpl(element);
    }

    @Override
    public void open() {
        element.click();
    }

    @Override
    public void select(int day) {
        open();
        LocalDate date = LocalDate.now();
        int today = date.getDayOfMonth();
        if (day > today) {
            selectDay(day);
        } else {
            element.findElement(By.xpath("//button[@aria-label='Next Month']")).click();
            selectDay(day);
        }
    }

    private void selectDay(int day) {
        Optional<WebElement> dayCell = driver.findElements(By.cssSelector("td.daycell")).stream()
                .filter(cell-> !cell.getAttribute("class").contains("disabled") &&
                        cell.getText().equals(String.valueOf(day))).findFirst();
        if (dayCell.isPresent()) {
            dayCell.get().click();
        } else {
            throw new NoSuchElementException("Couldn't select " + day + " from the Calender as either " +
                    "its not present or disabled");
        }
    }
}
