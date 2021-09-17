package lv.cebbys.mcmods.celib.processor.structures.types.customized.field;

import lv.cebbys.mcmods.celib.processor.structures.types.ClassProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.FieldProfile;
import lv.cebbys.mcmods.celib.processor.structures.customized.clazz.ClassProfileParser;

import javax.lang.model.element.Element;

public class BasicFieldProfile extends FieldProfile<BasicFieldProfile> {

    private BasicFieldProfile() {
    }

    public static BasicFieldProfile of() {
        BasicFieldProfile profile = new BasicFieldProfile();
        profile.setInstance(profile);
        return profile;
    }

    public static BasicFieldProfile of(Element e) {
        return BasicFieldProfile.of()
                .setName(e.getSimpleName().toString())
                .setType(ClassProfileParser.of(e))
                .setModifiers("private");
    }

    public static BasicFieldProfile of(ClassProfile<?> type, String name) {
        return BasicFieldProfile.of()
                .setType(type)
                .setName(name);
    }

    public static BasicFieldProfile of(Class<?> type, String name) {
        return BasicFieldProfile.of()
                .setType(ClassProfileParser.of(type))
                .setName(name);
    }

    public static BasicFieldProfile of(String modifiers, ClassProfile<?> type, String name) {
        return BasicFieldProfile.of(type, name)
                .setModifiers(modifiers.split("\s"));
    }

    @Override
    public String toParameter() {
        return getType().parameterString() + " " + getName();
    }
}
