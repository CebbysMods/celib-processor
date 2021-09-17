package lv.cebbys.mcmods.celib.processor.structures.types.customized.clazz;

import lv.cebbys.mcmods.celib.processor.structures.types.ClassProfile;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;

public class BasicClassProfile extends ClassProfile<BasicClassProfile> {

    private BasicClassProfile() {
    }

    public static BasicClassProfile of() {
        BasicClassProfile profile = new BasicClassProfile();
        return profile.setInstance(profile);
    }

    public static BasicClassProfile of(Element element) {
        return BasicClassProfile.of(element.asType());
    }

    public static BasicClassProfile of(Class<?> c) {
        BasicClassProfile profile = BasicClassProfile.of();
        profile.className = c.getSimpleName();
        profile.packageName = c.getPackageName();
        return profile;
    }

    public static BasicClassProfile of(String str) {
        BasicClassProfile profile = new BasicClassProfile();
        profile.className = str.substring(str.lastIndexOf(".") + 1);
        profile.packageName = str.substring(0, str.lastIndexOf("."));
        return profile;
    }

    public static BasicClassProfile of(TypeMirror type) {
        BasicClassProfile profile = BasicClassProfile.of();
        String sn = type.toString();
        if (sn.contains("<") || sn.contains(">")) {
            sn = sn.substring(0, sn.indexOf("<"));
        }
        profile.className = sn.substring(sn.lastIndexOf(".") + 1);
        profile.packageName = sn.substring(0, sn.lastIndexOf("."));
        return profile;
    }

    public static BasicClassProfile of(ArrayClassProfile type) {
        BasicClassProfile profile = BasicClassProfile.of();
        profile.setPackageName(type.getPackageName());
        profile.setClassName(type.getClassName());
        profile.setExtend(type.getExtend());
        profile.setImplements(list -> {
            list.addAll(type.getImplemented());
        });
        return profile;
    }

    @Override
    public ClassProfile<?> clone() {
        BasicClassProfile clone = BasicClassProfile.of();
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
}
