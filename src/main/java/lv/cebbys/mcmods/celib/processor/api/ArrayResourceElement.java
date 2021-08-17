package lv.cebbys.mcmods.celib.processor.api;

import java.util.ArrayList;
import java.util.List;

public @interface ArrayResourceElement {
    boolean split() default false;
    int elementCount() default 0;
    String[] elements() default {};
}
