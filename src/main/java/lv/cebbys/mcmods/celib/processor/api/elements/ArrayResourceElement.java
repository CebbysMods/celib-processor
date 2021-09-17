package lv.cebbys.mcmods.celib.processor.api.elements;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface ArrayResourceElement {
    String[] elements() default {};
}
