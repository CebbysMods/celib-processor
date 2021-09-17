package lv.cebbys.mcmods.celib.processor.structures.types.customized.clazz;

import lv.cebbys.mcmods.celib.processor.structures.types.ClassProfile;

import javax.lang.model.element.Element;

public class ArrayClassProfile extends ClassProfile<ArrayClassProfile> {

    private ArrayClassProfile() {}

    public static ArrayClassProfile of() {
        ArrayClassProfile profile = new ArrayClassProfile();
        profile.setInstance(profile);
        return profile;
    }

    public static ArrayClassProfile of(Element e) {
        return ArrayClassProfile.of(e.asType().toString());
    }

    public static ArrayClassProfile of(String s) {
        ArrayClassProfile profile = ArrayClassProfile.of();
        profile.setPackageName(s.substring(0, s.lastIndexOf(".")));
        profile.setClassName(s.substring(s.lastIndexOf(".") + 1, s.indexOf("[")));
        return profile;
    }

    public static ArrayClassProfile of(BasicClassProfile p) {
        ArrayClassProfile profile = ArrayClassProfile.of();
        profile.setClassName(p.getClassName());
        profile.setPackageName(p.getPackageName());
        profile.setExtend(p.getExtend());
        profile.setImplements(list -> list.addAll(p.getImplemented()));
        return profile;
    }



    @Override
    public ClassProfile<?> clone() {
        ArrayClassProfile clone = ArrayClassProfile.of();
        clone.setInstance(clone)
                .setPackageName(getPackageName())
                .setClassName(getClassName())
                .setExtend(getExtend() == null ? null : getExtend().clone())
                .setImplements(list -> {
                    getImplemented().forEach(i -> {
                        list.add(i.clone());
                    });
                });
        return clone;
    }

    @Override
    public String parameterString() {
        return super.parameterString() + "[]";
    }
}
