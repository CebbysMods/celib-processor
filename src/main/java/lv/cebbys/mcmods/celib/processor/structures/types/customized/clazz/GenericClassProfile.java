package lv.cebbys.mcmods.celib.processor.structures.types.customized.clazz;

import lv.cebbys.mcmods.celib.processor.structures.types.ClassProfile;

import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class GenericClassProfile extends ClassProfile<GenericClassProfile> {

    protected List<ClassProfile<?>> generics = new ArrayList<>();

    private GenericClassProfile() {}

    public static GenericClassProfile of() {
        GenericClassProfile profile = new GenericClassProfile();
        return profile.setInstance(profile);
    }

    public static GenericClassProfile of(ClassProfile<?> clazz, ClassProfile<?>... generics) {
        return GenericClassProfile.of()
                .setClassName(clazz.getClassName())
                .setPackageName(clazz.getPackageName())
                .setGenerics(generics);
    }

    public static GenericClassProfile of(Class<?> clazz, ClassProfile<?>... generics) {
        return GenericClassProfile.of(BasicClassProfile.of(clazz), generics);
    }

    public static GenericClassProfile of(Element e) {
        return GenericClassProfile.of(e.asType().toString());
    }

    public static GenericClassProfile of(String fullNameWithGenerics) {
        GenericClassProfile profile = new GenericClassProfile();
        final String fullName = fullNameWithGenerics.substring(0, fullNameWithGenerics.indexOf("<"));
        final String generics = fullNameWithGenerics.substring(
                fullNameWithGenerics.indexOf("<") + 1,
                fullNameWithGenerics.lastIndexOf(">")
        );
        profile.setPackageName(fullName.substring(0, fullName.lastIndexOf(".")));
        profile.setClassName(fullName.substring(fullName.lastIndexOf(".") + 1));
        profile.setGenerics(profiles -> {
            String gen = generics;
            while (gen.length() > 0 || !"".equals(gen)) {
                String substring;
                if(gen.contains(",")) {
                    substring = gen.substring(0, gen.indexOf(","));
                    gen = gen.substring(gen.indexOf(",") + 1);
                } else {
                    substring = gen;
                    gen = "";
                }
                profiles.add(profile(substring));
            }
        });
        return profile.setInstance(profile);
    }

    private static ClassProfile<?> profile(String str) {
        if(str.contains("<") || str.contains(">")) {
            return GenericClassProfile.of(str);
        } else {
            return BasicClassProfile.of(str);
        }
    }

    public GenericClassProfile setGenerics(Consumer<List<ClassProfile<?>>> consumer) {
        List<ClassProfile<?>> profiles = new ArrayList<>();
        consumer.accept(profiles);
        return setGenerics(profiles);
    }

    public GenericClassProfile setGenerics(ClassProfile<?>... g) {
        return setGenerics(Arrays.asList(g));
    }

    public GenericClassProfile setGenerics(List<ClassProfile<?>> g) {
        generics.clear();
        generics.addAll(g);
        return this;
    }

    public List<ClassProfile<?>> getGenerics() {
        return generics;
    }

    @Override
    public String parameterString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getClassName());
        builder.append("<");
        boolean first = true;
        for(ClassProfile<?> profile : generics) {
            if(first) {
                first = false;
            } else {
                builder.append(", ");
            }
            builder.append(profile.parameterString());
        }
        builder.append(">");
        return builder.toString();
    }

    @Override
    public ClassProfile<?> clone() {
        GenericClassProfile clone = GenericClassProfile.of();
        clone.setInstance(clone)
                .setPackageName(getPackageName())
                .setClassName(getClassName())
                .setExtend(getExtend() == null ? null : getExtend().clone())
                .setGenerics(list -> {
                    getGenerics().forEach(i -> {
                        list.add(i.clone());
                    });
                }).setImplements(list -> {
                    getImplemented().forEach(i -> {
                        list.add(i.clone());
                    });
                });
        return clone;
    }

    @Override
    public Set<ClassProfile<?>> getImports() {
        Set<ClassProfile<?>> set = super.getImports();
        set.addAll(generics);
        return set;
    }
}
