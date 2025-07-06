package hybridAutomation.Core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface Scan {
    /**
     * Holds image name
     * @return image name
     */
    String image() default "";

    /**
     * Holds target which can be Patter , or String or Image
     * @return Target class
     */
    Class<?> target() default String.class;
}
