package hybridAutomation.Core;

import org.python.antlr.ast.Str;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TestProperties {
    String[] id() default {};
}

