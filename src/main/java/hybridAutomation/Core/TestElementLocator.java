package hybridAutomation.Core;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.AbstractAnnotations;
import org.openqa.selenium.support.pagefactory.Annotations;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.Field;
import java.util.List;

public class TestElementLocator implements ElementLocator {

    private final SearchContext searchContext;
    private final boolean shouldCache;
    private final By by;
    private WebElement cachedElement;
    private List<WebElement> cachedElementList;

    public TestElementLocator(SearchContext searchContext, AbstractAnnotations annotations) {
        this.searchContext = searchContext;
        this.shouldCache = annotations.isLookupCached();
        this.by = annotations.buildBy();
    }

    public TestElementLocator(SearchContext searchContext, Field field) {
        this(searchContext, new Annotations(field));
    }


    @Override
    public WebElement findElement() {
        if (cachedElement != null && shouldCache) {
            return cachedElement;
        }
        WebElement element = searchContext.findElement(by);
        if (shouldCache) {
            cachedElement = element;
        }
        return element;
    }

    @Override
    public List<WebElement> findElements() {
        if (cachedElementList != null && shouldCache) {
            return cachedElementList;
        }
        List<WebElement> elements = searchContext.findElements(by);
        if (shouldCache) {
            cachedElementList = elements;
        }
        return elements;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " '" + by + "'";
    }
}
