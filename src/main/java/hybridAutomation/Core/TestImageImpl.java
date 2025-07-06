package hybridAutomation.Core;

import org.sikuli.script.Screen;

public class TestImageImpl implements TestImage{
    private final Screen screen;

    public TestImageImpl(Screen screen) {
        this.screen = screen;
    }
    @Override
    public boolean isDisplayed() {
        return screen.getLastMatch() != null;
    }

    @Override
    public int clickImage() {
        return screen.click();
    }
}
