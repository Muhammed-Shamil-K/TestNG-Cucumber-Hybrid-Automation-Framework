package hybridAutomation.Core;


import hybridAutomation.elements.TestElement;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementHandler;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementListHandler;

import java.lang.reflect.*;
import java.util.List;

public class TestFieldDecorator implements FieldDecorator {

    private final TestElementLocatorFactory factory;
    public TestFieldDecorator(TestElementLocatorFactory factory) {
        this.factory = factory;
    }

    @Override
    public Object decorate(ClassLoader loader, Field field) {
        if (!(WebElement.class.isAssignableFrom(field.getType()) || isCustomElement(field.getType()) || isDecoratableList(field))) {
            return null;
        }
        Class<?> fieldType = field.getType();
        if (WebElement.class.equals(fieldType)) {
            fieldType = TestElement.class;
        } else if (TestImage.class.equals(fieldType)) {
            return proxyForScan(loader, fieldType, factory.targetBuilder(field));
        }
        ElementLocator locator = factory.createLocator(field);
        if (locator == null) {
            return null;
        }
        if (WebElement.class.isAssignableFrom(fieldType) || isCustomElement(fieldType)) {
            return proxyForLocator(loader, fieldType, locator);
        }else if (List.class.isAssignableFrom(fieldType)) {
            Class<?> erasureClass = getErasureClass(field);
            return proxyForListLocator(loader, erasureClass, locator);
        }else {
            return null;
        }
    }

    private <T> boolean isCustomElement(Class<T> interfaceType) {
        return interfaceType.getAnnotation(CustomImplement.class) != null;
    }

    protected boolean isDecoratableList(Field field) {
        if (!List.class.isAssignableFrom(field.getType())) {
            return false;
        }
        Class<?> erasureClass = getErasureClass(field);
        if (erasureClass == null || !(WebElement.class.isAssignableFrom(erasureClass) || isCustomElement(erasureClass))) {
            return false;
        }
        return field.getAnnotation(FindBy.class) != null ||
                field.getAnnotation(FindBys.class) != null ||
                field.getAnnotation(FindAll.class) != null ||
                field.getAnnotation(Scan.class) != null;
    }

    private Class<?> getErasureClass(Field field) {
        Type genericType = field.getGenericType();
        return (Class<?>) ((ParameterizedType) genericType).getActualTypeArguments()[0];
    }

    /*
    Generate a type-parameterized locator proxy for the element in question
     */
    protected <T> T proxyForLocator(ClassLoader loader, Class<T> interfaceType, ElementLocator locator) {
        InvocationHandler handler;
        if (interfaceType.getAnnotation(CustomImplement.class) != null) {
            handler = new TestElementHandler(interfaceType, locator);
        }else {
            handler = new LocatingElementHandler(locator);
        }

        T proxy;
        //here where the problem lies.........
        proxy = interfaceType.cast(Proxy.newProxyInstance(
                loader, new Class[]{interfaceType, WebElement.class, WrapsElement.class, Locatable.class}, handler
        ));
        return proxy;
    }

    /* Generates a proxy for the list of elements to be wrapped*/
    protected <T> List<T> proxyForListLocator(ClassLoader loader, Class<T> interfaceType, ElementLocator locator) {
        InvocationHandler handler;
        if (interfaceType.getAnnotation(CustomImplement.class) != null) {
            handler = new TestElementListHandler(interfaceType, locator);
        }else {
            handler = new LocatingElementListHandler(locator);
        }
        List<T> proxy;
        proxy = (List<T>) Proxy.newProxyInstance(loader, new Class[]{List.class}, handler);
        return proxy;
    }

    protected <T> T proxyForScan(ClassLoader loader, Class<T> interfaceType, ImageTargetBuilder targetBuilder) {
        InvocationHandler handler = new TestImageHandler(interfaceType, targetBuilder);
        return interfaceType.cast(Proxy.newProxyInstance(loader, new Class[]{interfaceType}, handler));
    }
}
