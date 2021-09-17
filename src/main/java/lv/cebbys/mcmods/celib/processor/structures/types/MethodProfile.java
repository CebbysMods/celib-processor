package lv.cebbys.mcmods.celib.processor.structures.types;

import lv.cebbys.mcmods.celib.processor.interfaces.ImportProvider;
import lv.cebbys.mcmods.celib.processor.interfaces.IntendString;
import lv.cebbys.mcmods.celib.processor.structures.customized.clazz.ClassProfileParser;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.clazz.BasicClassProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.clazz.GenericParameterClassProfile;
import lv.cebbys.mcmods.celib.processor.utility.ModifierComparator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class MethodProfile<T> implements IntendString, ImportProvider {

    protected T instance;
    protected Set<ClassProfile<?>> additionalImports = new LinkedHashSet<>();
    protected Set<String> modifiers = new LinkedHashSet<>();
    protected ClassProfile<?> returnType = null;
    protected String methodName;
    protected Set<FieldProfile<?>> parameters = new LinkedHashSet<>();
    protected List<String> codeLines = new LinkedList<>();


    protected T setInstance(T i) {
        instance = i;
        return instance;
    }

    public Set<ClassProfile<?>> getImports() {
        Set<ClassProfile<?>> imports = new HashSet<>();
        if(returnType != null && !(returnType instanceof GenericParameterClassProfile)) {
            imports.add(returnType);
        }
        parameters.forEach(param -> {
            imports.add(param.getType());
        });
        imports.addAll(additionalImports);
        return imports;
    }

    public T addAdditionalImports(ClassProfile<?>... i) {
        additionalImports.addAll(Arrays.asList(i));
        return instance;
    }

    public T addAdditionalImports(Class<?>... i) {
        additionalImports.addAll(Arrays.stream(i).map(ClassProfileParser::of).collect(Collectors.toSet()));
        return instance;
    }

    public T setReturnType(ClassProfile<?> r) {
        returnType = r;
        return instance;
    }

    public T setReturnType(Class<?> r) {
        return setReturnType(ClassProfileParser.of(r));
    }


    public T setModifiers(List<String> i) {
        modifiers.clear();
        modifiers.addAll(i);
        return instance;
    }

    public T setModifiers(String... i) {
        return setModifiers(Arrays.asList(i));
    }
    public T setModifiers(Consumer<List<String>> i) {
        List<String> list = new ArrayList<>();
        i.accept(list);
        return setModifiers(list);
    }

    public T addModifiers(String... i) {
        return addModifiers(Arrays.asList(i));
    }

    public T addModifiers(List<String> i) {
        modifiers.addAll(i);
        return instance;
    }

    public T setParameters(List<FieldProfile<?>> consumer) {
        parameters.clear();
        parameters.addAll(consumer);
        return instance;
    }

    public T setParameters(FieldProfile<?>... consumer) {
        return setParameters(Arrays.asList(consumer));
    }

    public T setParameters(Consumer<List<FieldProfile<?>>> consumer) {
        List<FieldProfile<?>> list = new ArrayList<>();
        consumer.accept(list);
        return setParameters(list);
    }

    public T addParameters(FieldProfile<?>... p) {
        return addParameters(Arrays.asList(p));
    }

    public T addParameters(List<FieldProfile<?>> p) {
        parameters.addAll(p);
        return instance;
    }

    public T addLine(String format, Object... params) {
        codeLines.add(String.format(format, params));
        return instance;
    }

    public T setMethodName(String name) {
        methodName = name;
        return instance;
    }

    public boolean isStatic() {
        return modifiers.contains("static");
    }

    @Override
    public String toString(int intends) {
        StringBuilder builder = new StringBuilder();
        intend(builder, intends);
        List<String> mods = modifiers.stream().sorted(ModifierComparator.INSTANCE).collect(Collectors.toList());
        for(String mod : mods) {
            builder.append(mod).append(" ");
        }
        builder.append(returnType == null ? "void" : returnType.parameterString()).append(" ");
        builder.append(methodName).append("(");
        boolean first = true;
        for(FieldProfile<?> param : sortedParameters(parameters)) {
            if(first) {
                first = false;
            } else {
                builder.append(", ");
            }
            builder.append(param.toParameter());
        }
        builder.append(") {\n");
        codeLines.forEach(line -> {
            intend(builder, intends + 1).append(line).append("\n");
        });
        intend(builder, intends).append("}\n");
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof MethodProfile) {
            return ((MethodProfile<?>) o).toString(0).equals(this.toString(0));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.toString(0).hashCode();
    }

    protected List<FieldProfile<?>> sortedParameters(Set<FieldProfile<?>> parameters) {
        return parameters.stream()
                .sorted(Comparator.comparingInt(FieldProfile::getIndex))
                .collect(Collectors.toList());
    }
}
