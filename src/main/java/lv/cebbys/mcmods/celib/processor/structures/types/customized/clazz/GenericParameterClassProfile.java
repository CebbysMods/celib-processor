package lv.cebbys.mcmods.celib.processor.structures.types.customized.clazz;

import lv.cebbys.mcmods.celib.processor.structures.types.ClassProfile;

public class GenericParameterClassProfile extends ClassProfile<GenericParameterClassProfile> {

    private GenericParameterClassProfile() {}

    public static GenericParameterClassProfile of() {
        GenericParameterClassProfile profile = new GenericParameterClassProfile();
        profile.setInstance(profile);
        return profile;
    }

    @Override
    public ClassProfile<?> clone() {
        GenericParameterClassProfile clone = GenericParameterClassProfile.of();
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
        return getClassName();
    }

    @Deprecated
    @Override
    public GenericParameterClassProfile setPackageName(String n) {
        throw new RuntimeException("Generic parameter types cant have package names");
    }

    @Deprecated
    @Override
    public String getPackageName() {
//        throw new RuntimeException("Generic parameter types cant have package names");
        return "";
    }

    @Deprecated
    @Override
    public String getFullName() {
//        throw new RuntimeException("Generic parameter types cant have package names");
        return "";
    }

    @Deprecated
    @Override
    public String importString() {
//        throw new RuntimeException("Generic parameter types cant be imported");
        return "";
    }

}
