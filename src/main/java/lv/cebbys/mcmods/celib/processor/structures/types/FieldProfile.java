package lv.cebbys.mcmods.celib.processor.structures.types;

import lv.cebbys.mcmods.celib.processor.interfaces.ImportProvider;
import lv.cebbys.mcmods.celib.processor.interfaces.IntendString;
import lv.cebbys.mcmods.celib.processor.utility.ModifierComparator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class FieldProfile<T> implements IntendString, ImportProvider {

    protected int index;
    protected T instance;
    protected List<String> modifiers = new ArrayList<>();
    protected String name;
    protected ClassProfile<?> type;

    public abstract String toParameter();

    public String getName() {
        return name;
    }

    public ClassProfile<?> getType() {
        return type;
    }

    public T setName(String n) {
        name = n;
        return instance;
    }

    public T setType(ClassProfile<?> t) {
        type = t;
        return instance;
    }

    public T setModifiers(String... m) {
        return setModifiers(Arrays.asList(m));
    }

    public T setModifiers(List<String> mod) {
        modifiers.clear();
        List<String> accessModifiers = Arrays.asList(ModifierComparator.ACCESS_MODIFIERS);
        mod.forEach(m -> {
            if(accessModifiers.contains(m) && modifiers.stream().noneMatch(accessModifiers::contains)) {
                modifiers.add(m);
            } else if(ModifierComparator.STATIC_MODIFIER.equals(m)) {
                modifiers.add(m);
            } else if(ModifierComparator.FINAL_MODIFIER.equals(m)) {
                modifiers.add(m);
            }
        });
        return instance;
    }

    public boolean isStatic() {
        return modifiers.contains("static");
    }

    @Override
    public String toString(int intends) {
        StringBuilder builder = new StringBuilder();
        intend(builder, intends);
        modifiers = modifiers.stream().sorted(ModifierComparator.INSTANCE).collect(Collectors.toList());
        modifiers.forEach(modifier -> {
            builder.append(modifier).append(" ");
        });
        builder.append(type.parameterString()).append(" ").append(name).append(";\n");
        return builder.toString();
    }

    public T setInstance(T i) {
        instance = i;
        return instance;
    }

    public T setParameterIndex(int i) {
        index = i;
        return instance;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public Set<ClassProfile<?>> getImports() {
        Set<ClassProfile<?>> set = new HashSet<>();
        if(getType() != null) {
            set.add(getType());
        }
        return set;
    }
}
