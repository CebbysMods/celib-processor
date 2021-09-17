package lv.cebbys.mcmods.celib.processor.structures.types.customized.method;

import lv.cebbys.mcmods.celib.processor.structures.types.ClassProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.FieldProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.MethodProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.clazz.ConsumerClassProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.field.BasicFieldProfile;

import javax.lang.model.element.Element;

public class BasicMethodProfile extends MethodProfile<BasicMethodProfile> {

    private BasicMethodProfile() {
    }

    public static BasicMethodProfile of() {
        BasicMethodProfile profile = new BasicMethodProfile();
        return profile.setInstance(profile);
    }
}
