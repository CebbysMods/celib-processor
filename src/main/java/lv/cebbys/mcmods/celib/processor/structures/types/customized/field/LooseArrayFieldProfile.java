package lv.cebbys.mcmods.celib.processor.structures.types.customized.field;

import lv.cebbys.mcmods.celib.processor.structures.types.ClassProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.FieldProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.clazz.ConsumerClassProfile;

public class LooseArrayFieldProfile extends FieldProfile<LooseArrayFieldProfile> {

    private LooseArrayFieldProfile() {
    }

    public static LooseArrayFieldProfile of() {
        LooseArrayFieldProfile profile = new LooseArrayFieldProfile();
        profile.setInstance(profile);
        return profile;
    }

    public static LooseArrayFieldProfile ofConsumers(ClassProfile<?> consumables) {
        return LooseArrayFieldProfile.of()
                .setName("consumers")
                .setType(ConsumerClassProfile.ofConsumer(consumables));
    }

    @Override
    public String toParameter() {
        return getType().parameterString() + "... " + getName();
    }
}
