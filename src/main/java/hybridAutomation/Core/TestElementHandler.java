package hybridAutomation.Core;

import hybridAutomation.Utilities.RetryUtil;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import hybridAutomation.reporting.TestLog;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import static hybridAutomation.Core.CustomElementProcessor.getWrapperClass;

public class TestElementHandler implements InvocationHandler {
    private final ElementLocator locator;
    private final Class<?> wrappingType;

    /*
    Generate a handler to retrieve the WebElement from a locator for a given WebElement interface descendant.
     */
    public <T> TestElementHandler(Class<T> interfaceType, ElementLocator locator) {
        this.locator = locator;
        if (interfaceType.getAnnotation(CustomImplement.class) == null) {
            throw new RuntimeException("Interface " + interfaceType.getCanonicalName() + " not assignable to element." +
                    " Apply @CustomImplement to interface");
        }
        this.wrappingType = getWrapperClass(interfaceType);
    }
    @Override
    public Object invoke(Object object, Method method, Object[] objects) throws Throwable {
        WebElement element;
        try {
            Optional<WebElement> optional = locator.findElements().stream()
                    .filter(e-> Integer.parseInt(e.getAttribute("offsetHeight")) > 0 &&
                            Integer.parseInt(e.getAttribute("offsetWidth")) > 0).findFirst();
            element = optional.orElseGet(locator::findElement);
        }catch (NoSuchElementException e) {
            //returning proxy for lazy loading
            if ("toString".equals(method.getName())) {
                return "Proxy element for: " + locator.toString();
            }
            //should not retry if element is not intended to display
            if ("isDisplayed".equals(method.getName())) {
                TestLog.log().info("Element " +locator.toString() + " is not displayed.");
                return false;
            }
            //trying auto synchronization with application under test
            element = RetryUtil.retry(locator::findElement, 10, "locating element using " + locator.toString());
        }
        //throwing no such element exception if element is null
        if (element == null) {
            throw new NoSuchElementException(locator.toString());
        }
        if ("getWrappedElement".equals(method.getName())) {
            return element;
        }
        Constructor<?> constructor = wrappingType.getConstructor(WebElement.class);
        Object thing = constructor.newInstance(element);
        try{
            return method.invoke(wrappingType.cast(thing), objects);
        }catch (InvocationTargetException e) {
            //unwrap the underlying exception
            throw  e.getCause();
        }
    }
}
