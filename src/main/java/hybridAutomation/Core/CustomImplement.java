package hybridAutomation.Core;

import hybridAutomation.elements.TestElement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomImplement {
    Class<?> value() default TestElement.class;
}
