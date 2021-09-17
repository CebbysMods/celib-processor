package lv.cebbys.mcmods.celib.processor.structures.customized.clazz;

import lv.cebbys.mcmods.celib.processor.structures.types.ClassProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.clazz.ArrayClassProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.clazz.BasicClassProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.clazz.GenericClassProfile;

import javax.lang.model.element.Element;

public class ClassProfileParser {

    public static ClassProfile<?> of(Element e) {
        return ClassProfileParser.of(e.asType().toString());
    }

    public static ClassProfile<?> of(Class<?> e) {
        return ClassProfileParser.of(e.getName());
    }

    public static ClassProfile<?> of(String type) {
        if(type.contains("<") || type.contains(">")) {
            return GenericClassProfile.of(type);
        } else if(type.contains("[") || type.contains("]")){
            return ArrayClassProfile.of(type);
        } else {
            return BasicClassProfile.of(type);
        }
    }

}
