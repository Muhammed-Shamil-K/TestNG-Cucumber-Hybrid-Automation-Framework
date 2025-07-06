package hybridAutomation.Core;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Screen;
import hybridAutomation.reporting.TestLog;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestImageHandler implements InvocationHandler {
    private final Class<?> wrappingType;
    private Screen screen;
    private final ImageTargetBuilder target;

    public <T> TestImageHandler(Class<T> interfaceType, ImageTargetBuilder targetBuilder) {
        //implicitly disabling headless mode
        System.setProperty("java.awt.headless", "false");
        try {
            this.screen = new Screen();
        }catch (Exception e) {
            TestLog.log().error("Could not initialize screen ");
        }
        if (interfaceType.getAnnotation(CustomImplement.class) == null) {
            throw new RuntimeException("Interface " + interfaceType.getCanonicalName() + " not assignable to element."+
            " Apply @CustomImplement to interface");
        }
        this.wrappingType = CustomElementProcessor.getWrapperClass(interfaceType);
        this.target = targetBuilder;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object psi = target.build();
        try{
            screen.find(psi);
        }catch (FindFailed e) {
            if ("toString".equals(method.getName())) {
                return "Proxy element for: " + psi.toString();
            }
            if ("waitUntilExist".equals(method.getName())) {
                screen.wait(psi, (long) args[0]);
            }else {
                TestLog.log().error("FindFailed!!!!");
                throw new RuntimeException(e);
            }
        }
        Constructor<?> constructor = wrappingType.getConstructor(Screen.class);
        Object thing = constructor.newInstance(screen);
        try{
            return method.invoke(wrappingType.cast(thing), args);
        }catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
