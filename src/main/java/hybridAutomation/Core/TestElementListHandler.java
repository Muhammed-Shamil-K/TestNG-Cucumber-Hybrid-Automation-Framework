package hybridAutomation.Core;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static hybridAutomation.Core.CustomElementProcessor.getWrapperClass;

public class TestElementListHandler implements InvocationHandler {
    private final ElementLocator locator;
    private final Class<?> wrappingType;

    public <T> TestElementListHandler(Class<T> interfaceType, ElementLocator locator) {
        this.locator = locator;
        if (interfaceType.getAnnotation(CustomImplement.class) == null) {
            throw new RuntimeException("Interface " + interfaceType.getCanonicalName() + " not assignable to element." +
                    " Apply @CustomImplement to interface");
        }
        this.wrappingType = getWrapperClass(interfaceType);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] objects) throws Throwable {
        List<Object> wrappedList = new ArrayList<>();
        Constructor<?> constructor = wrappingType.getConstructor(WebElement.class);
        for (WebElement element : locator.findElements()) {
            Object thing = constructor.newInstance(element);
            wrappedList.add(wrappingType.cast(thing));
        }
        try{
            return method.invoke(wrappedList, objects);
        }catch (InvocationTargetException e) {
            //Unwrap the underlying exception
            throw e.getCause();
        }
    }
}
