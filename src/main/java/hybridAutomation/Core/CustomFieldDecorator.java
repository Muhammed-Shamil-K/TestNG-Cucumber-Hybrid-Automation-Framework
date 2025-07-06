package hybridAutomation.Core;

import hybridAutomation.elements.Impl.TestElementImpl;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

public class CustomFieldDecorator extends DefaultFieldDecorator {

    public CustomFieldDecorator(ElementLocatorFactory factory) {
        super(factory);
    }

    @Override
    protected WebElement proxyForLocator(ClassLoader loader, ElementLocator locator) {
        return new TestElementImpl(super.proxyForLocator(loader, locator));
    }

}
