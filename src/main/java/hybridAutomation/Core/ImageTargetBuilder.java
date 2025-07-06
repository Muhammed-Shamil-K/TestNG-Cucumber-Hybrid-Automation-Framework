package hybridAutomation.Core;

import org.testng.Assert;
import hybridAutomation.reporting.TestLog;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Objects;

public class ImageTargetBuilder {
    private final Field field;
    public ImageTargetBuilder(Field field) {
        this.field = field;
    }
    public Object build() throws Throwable {
        Assert.assertNotNull(field.getAnnotation(Scan.class));
        Scan scan = field.getAnnotation(Scan.class);
        Class<?> clazz = Class.forName(scan.target().getName());
        Constructor<?> constructor = clazz.getConstructor(String.class);
        try{
            return constructor.newInstance(Objects.requireNonNull(ImageTargetBuilder.class.getClassLoader().getResource(scan.image())).getPath());
        }catch (Exception e) {
            TestLog.log().error("Could not find image " + scan.image());
            throw new RuntimeException(e.getMessage());
        }

    }
}
