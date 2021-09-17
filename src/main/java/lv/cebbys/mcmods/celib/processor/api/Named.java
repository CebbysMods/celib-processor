package lv.cebbys.mcmods.celib.processor.api;

import lv.cebbys.mcmods.celib.processor.utility.DefinedName;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Named {
    DefinedName[] predefines() default {};
}
