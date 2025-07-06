package hybridAutomation.Core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;

import java.lang.reflect.Constructor;

public class TestPageFactory {
    public static <T> T initElements(WebDriver driver, Class<T> pageClassToProxy) {
        T page = instantiatePage(driver, pageClassToProxy);
        return initElements(driver, page);
    }

    public static <T> T initElements(WebDriver driver, T page) {
        initElements(new TestElementLocatorFactory(driver), page);
        return page;
    }

    public static void initElements(TestElementLocatorFactory factory, Object page) {
        initElements(new TestFieldDecorator(factory), page);
    }

    public static void initElements(FieldDecorator decorator, Object page) {
        PageFactory.initElements(decorator, page);
    }

    private static <T> T instantiatePage(WebDriver driver, Class<T> pageClassToProxy) {
        try {
            try {
                Constructor<T> constructor = pageClassToProxy.getConstructor(WebDriver.class);
                return constructor.newInstance(driver);
            }catch (NoSuchMethodException e) {
                return pageClassToProxy.getDeclaredConstructor().newInstance();
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
