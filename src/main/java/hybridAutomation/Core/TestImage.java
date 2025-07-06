package hybridAutomation.Core;

/**
 * Special custom element for image based validations
 * Only @Scan annotation is allowed for image element
 * Finds matching screen region based on Pattern or String or TestImage
 * Note : @FindBy/@FindBys/@FindAll annotations are not supported
 */
@CustomImplement(value = TestImageImpl.class)
public interface TestImage {
    /**
     * Scan for the best match on the screen
     * @return true if target match is found, else false
     */
    boolean isDisplayed();

    /**
     * click on the screen region based on the image match
     * @return 1 if successful , 0 otherwise
     */
    int clickImage();
}
