package hybridAutomation.Core;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.Annotations;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import java.lang.reflect.Field;

public class TestElementLocatorFactory implements ElementLocatorFactory {

    private final SearchContext searchContext;
    public TestElementLocatorFactory(SearchContext searchContext) {
        this.searchContext = searchContext;
    }
    @Override
    public ElementLocator createLocator(Field field) {
        return new TestElementLocator(this.searchContext, new Annotations(field));
    }

    public ImageTargetBuilder targetBuilder(Field field) {
        return new ImageTargetBuilder(field);
    }
}
