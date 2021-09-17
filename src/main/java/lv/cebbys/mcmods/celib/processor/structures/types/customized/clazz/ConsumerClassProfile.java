package lv.cebbys.mcmods.celib.processor.structures.types.customized.clazz;

import lv.cebbys.mcmods.celib.processor.structures.types.ClassProfile;

import java.util.Set;
import java.util.function.Consumer;

public class ConsumerClassProfile extends ClassProfile<ConsumerClassProfile> {

    protected ClassProfile<?> consumes;

    private ConsumerClassProfile() {}

    public static ConsumerClassProfile of() {
        ConsumerClassProfile profile = new ConsumerClassProfile();
        profile.className = Consumer.class.getSimpleName();
        profile.packageName = Consumer.class.getPackageName();
        return profile.setInstance(profile);
    }

    public static ConsumerClassProfile ofConsumer(ClassProfile<?> consumer) {
        return ConsumerClassProfile.of().setConsumer(consumer);
    }

    public ConsumerClassProfile setConsumer(ClassProfile<?> c) {
        consumes = c;
        return this;
    }

    public ClassProfile<?> getConsumer() {
        return consumes;
    }

    @Override
    public ClassProfile<?> clone() {
        ConsumerClassProfile clone = ConsumerClassProfile.of();
        clone.setInstance(clone)
                .setPackageName(getPackageName())
                .setClassName(getClassName())
                .setExtend(getExtend() == null ? null : getExtend().clone())
                .setConsumer(getConsumer().clone())
                .setImplements(list -> {
                    getImplemented().forEach(i -> {
                        list.add(i.clone());
                    });
                });
        return clone;
    }

    @Deprecated
    @Override
    public ConsumerClassProfile setClassName(String n) {
        return this;
    }

    @Deprecated
    @Override
    public ConsumerClassProfile setPackageName(String n) {
        return this;
    }

    @Override
    public String parameterString() {
        return getClassName() + "<" + getConsumer().parameterString() + ">";
    }

    @Override
    public Set<ClassProfile<?>> getImports() {
        Set<ClassProfile<?>> set = super.getImports();
        set.add(consumes);
        return set;
    }
}
