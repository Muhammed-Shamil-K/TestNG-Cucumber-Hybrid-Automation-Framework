package hybridAutomation.Core;

import org.openqa.selenium.*;
import org.testng.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ByDomExpansion extends By implements Serializable {

    private By elementSelector;
    private By[] shadowSelector = {};
    private WebDriver driver;

    //anchor element to restrict the greedy search
    private WebElement anchor;

    private static final By DEFAULT_SELECTOR = By.cssSelector("*");

    public ByDomExpansion(WebDriver driver) {
        this.driver = driver;
        this.elementSelector = DEFAULT_SELECTOR;
    }

    public ByDomExpansion(By elementSelector, By... shadow) {
        this.elementSelector = elementSelector;
        this.shadowSelector = shadow;
    }

    public void setAnchorElement(WebElement anchorElement) {
        this.anchor = anchorElement;
    }

    @Override
    public String toString() {
        return elementSelector.toString();
    }

    private WebElement findElementUsingShadowSelector(WebDriver driver) {
        SearchContext context = getContext(driver);
        try {
            return context.findElement(elementSelector);
        }catch (NoSuchElementException e) {
            List<SearchContext> contexts = new ArrayList<>();
            try {
                getShadowRootList(context.findElements(DEFAULT_SELECTOR), contexts);
                return iterateRootAndFindElement(contexts);
            } catch (InvalidSelectorException invalidSelectorException) {
                throw new NoSuchElementException("Couldn't find element using locator " + toString());
            }
        }
    }

    private List<WebElement> findElementsUsingShadowSelector(WebDriver driver) {
        List<WebElement> elements = new ArrayList<>();
        List<SearchContext> contexts = new ArrayList<>();
        try {
            SearchContext context = getContext(driver);
            elements = context.findElements(elementSelector);
            if (elements.size() ==0) {
                getShadowRootList(context.findElements(DEFAULT_SELECTOR), contexts);
            }
        } catch (NoSuchElementException e) {
            getShadowRootList(driver.findElements(DEFAULT_SELECTOR), contexts);
        }

        for (SearchContext root : contexts) {
            elements.addAll(root.findElements(this.elementSelector));
        }

        return elements;
    }

    private WebElement iterateRootAndFindElement(List<SearchContext> contexts) {
        for (SearchContext context : contexts) {
            try {
                return context.findElement(this.elementSelector);
            } catch (Exception e) {
                e.getSuppressed();
            }
        }
        throw new NoSuchElementException("Couldn't find element using the locator " + toString());
    }

    private List<WebElement> getNodes() {
        List<WebElement> nodes = new ArrayList<>();
        if (anchor == null) {
            nodes.addAll(driver.findElements(DEFAULT_SELECTOR));
        } else {
            nodes.add(anchor);
        }
        return nodes;
    }

    private WebElement findShadowRootElement() {
        List<SearchContext> contexts = new ArrayList<>();
        getShadowRootList(getNodes(), contexts);
        return iterateRootAndFindElement(contexts);
    }

    private List<WebElement> findShadowRootElements() {
        List<WebElement> elements = new ArrayList<>();
        List<SearchContext> contexts = new ArrayList<>();
        getShadowRootList(getNodes(), contexts);
        for (SearchContext context : contexts) {
            elements.addAll(context.findElements(this.elementSelector));
        }
        return elements;
    }

    private void getShadowRootList(List<WebElement> elements, List<SearchContext> contexts) {
        elements.forEach(element -> {
            try {
                SearchContext context = element.getShadowRoot();
                contexts.add(context);
                getShadowRootList(context.findElements(DEFAULT_SELECTOR), contexts);
            } catch (NoSuchElementException e) {
                e.getSuppressed();
            }
        });
    }

    private SearchContext getContext(WebDriver driver) {
        WebElement element;
        SearchContext context = null;
        for (By selector : shadowSelector) {
            if (context == null) {
                element = driver.findElement(selector);
            } else {
                element = context.findElement(selector);
            }
            context = element.getShadowRoot();
        }
        return context;
    }

    @Override
    public List<WebElement> findElements(SearchContext context) {
        List<WebElement> elements;
        this.driver = (WebDriver) context;
        if (shadowSelector.length < 0) {
            elements = findElementsUsingShadowSelector(driver);
        } else {
            if (anchor != null) {
                elements = anchor.findElements(elementSelector);
            } else {
                elements = driver.findElements(elementSelector);
            }

            if (elements.size() == 0) {
                elements = findShadowRootElements();
            }
        }
        return elements;
    }

    @Override
    public WebElement findElement(SearchContext context) {
        this.driver = (WebDriver) context;
        try {
            return driver.findElement(elementSelector);
        } catch (NoSuchElementException e) {
            if (shadowSelector.length == 0) {
                return findShadowRootElement();
            }
            return findElementUsingShadowSelector(driver);
        }
    }

    public WebElement findElement(By locator, By... shadowLocators) {
        Assert.assertNotNull(this.driver, "initialize " + this.getClass().getSimpleName() + " with WebDriver");
        this.elementSelector = locator;
        this.shadowSelector = shadowLocators;
        return findElement(this.driver);
    }

    public List<WebElement> findElements(By locator, By... shadowLocators) {
        Assert.assertNotNull(this.driver, "initialize " + this.getClass().getSimpleName() + " with WebDriver");
        this.elementSelector = locator;
        this.shadowSelector = shadowLocators;
        return findElements(this.driver);
    }
}