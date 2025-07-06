package hybridAutomation.elements;

import hybridAutomation.Core.CustomImplement;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.interactions.Locatable;

@CustomImplement
public interface TestElement extends WebElement, WrapsElement, Locatable {

    boolean click2();

    boolean click3();
}
