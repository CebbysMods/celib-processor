package lv.cebbys.mcmods.celib.processor.api.elements;


import lv.cebbys.mcmods.celib.processor.utility.MappingMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface ListResourceElement {
    MappingMethod[] mappingMethods() default {};
}
