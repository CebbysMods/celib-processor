package lv.cebbys.mcmods.celib.processor.structures.types;

import lv.cebbys.mcmods.celib.processor.interfaces.FieldsProvider;
import lv.cebbys.mcmods.celib.processor.interfaces.ImportProvider;
import lv.cebbys.mcmods.celib.processor.interfaces.IntendString;
import lv.cebbys.mcmods.celib.processor.interfaces.MethodProvider;
import lv.cebbys.mcmods.celib.processor.interfaces.MethodsAndFieldsProvider;
import lv.cebbys.mcmods.celib.processor.interfaces.MethodsProvider;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.clazz.BasicClassProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.clazz.GenericParameterClassProfile;
import lv.cebbys.mcmods.celib.processor.structures.customized.ResourceArrayMethodsAndFields;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ContainerProfile implements IntendString, ImportProvider {

    protected List<FieldProfile<?>> fields = new ArrayList<>();
    protected List<MethodProfile<?>> methods = new ArrayList<>();
    protected ClassProfile<?> profile = BasicClassProfile.of();


    public ContainerProfile() {

    }

    public ContainerProfile setExtend(ClassProfile<?> e) {
        profile.setExtend(e);
        return this;
    }

    public ContainerProfile addImplement(ClassProfile<?> i) {
        profile.addImplement(i);
        return this;
    }

    public ContainerProfile addMethod(ResourceArrayMethodsAndFields method) {
        addField(method.getField());
        return addMethod(method.getMethod());
    }

    public ContainerProfile addMethod(MethodProfile<?> method) {
        methods.add(method);
        return this;
    }

    public ContainerProfile addMethod(MethodProvider provider) {
        return addMethod(provider.getMethod());
    }

    public ContainerProfile addMethods(MethodsProvider provider) {
        provider.getMethods().forEach(this::addMethod);
        return this;
    }

    public ContainerProfile addMethodsAndFields(MethodsAndFieldsProvider provider) {
        addMethods(provider);
        return addFields(provider);
    }

    public ContainerProfile addField(FieldProfile<?> p) {
        fields.add(p);
        return this;
    }

    public ContainerProfile addFields(FieldsProvider provider) {
        provider.getFields().forEach(this::addField);
        return this;
    }

    public ClassProfile<?> getProfile() {
        return profile;
    }

    public ContainerProfile setProfile(ClassProfile<?> p) {
        profile = p;
        return this;
    }
    @Override
    public String toString(int intends) {
        StringBuilder builder = new StringBuilder();
        toStringComment(builder, intends);
        builder.append("\n");
        toStringPackage(builder, intends);
        builder.append("\n");
        if (getImports().size() > 0) {
            toStringImports(builder, intends);
            builder.append("\n");
        }
        toStringClass(builder, intends);
        builder.append("\n");
        toStringFields(builder, intends + 1);
        builder.append("\n");
        toStringMethods(builder, intends + 1);
        intend(builder, intends).append("}\n");
        return builder.toString();
    }

    private void toStringMethods(StringBuilder builder, int i) {
        List<MethodProfile<?>> staticMethods = methods.stream().filter(MethodProfile::isStatic).collect(Collectors.toList());
        if (staticMethods.size() > 0) {
            staticMethods.forEach(f -> {
                builder.append(f.toString(i));
                builder.append("\n");
            });
        }
        List<MethodProfile<?>> normalMethods = methods.stream().filter(f -> !f.isStatic()).collect(Collectors.toList());
        if (normalMethods.size() > 0) {
            normalMethods.forEach(f -> {
                builder.append(f.toString(i));
                builder.append("\n");
            });
        }
    }

    private void toStringFields(StringBuilder builder, int intends) {
        List<FieldProfile<?>> staticFields = fields.stream().filter(FieldProfile::isStatic).collect(Collectors.toList());
        if (staticFields.size() > 0) {
            staticFields.forEach(f -> builder.append(f.toString(intends)));
            builder.append("\n");
        }
        List<FieldProfile<?>> normalFields = fields.stream().filter(f -> !f.isStatic()).collect(Collectors.toList());
        if (normalFields.size() > 0) {
            normalFields.forEach(f -> builder.append(f.toString(intends)));
        }
    }

    private void toStringClass(StringBuilder builder, int intends) {
        intend(builder, intends).append("public class ").append(profile.getClassName());
        if (profile.getExtend() != null) {
            builder.append(" extends ").append(profile.getExtend().getClassName());
        }
        if (profile.getImplemented().size() > 0) {
            boolean first = true;
            builder.append(" implements ");
            for (ClassProfile<?> i : profile.getImplemented()) {
                if (first) {
                    first = false;
                } else {
                    builder.append(", ");
                }
                builder.append(i.getClassName());
            }
        }
        builder.append(" {\n");
    }

    private void toStringImports(StringBuilder builder, int intends) {
        getImports().forEach(i -> intend(builder, intends).append("import ").append(i.getFullName()).append(";\n"));
    }

    private void toStringPackage(StringBuilder builder, int intends) {
        intend(builder, intends).append("package ").append(profile.getPackageName()).append(";\n");
    }

    private void toStringComment(StringBuilder builder, int intends) {
        intend(builder, intends).append("// This class was generated by CebbyS Celib Processor\n");
    }

    @Override
    public Set<ClassProfile<?>> getImports() {
        Set<ClassProfile<?>> imports = new HashSet<>(getProfile().getImports());
        methods.forEach(method -> imports.addAll(method.getImports()));
        fields.forEach(field -> imports.addAll(field.getImports()));
        return imports.stream()
                .filter(i -> !(i instanceof GenericParameterClassProfile))
                .filter(i -> !getProfile().getPackageName().equals(i.getPackageName()))
                .collect(Collectors.toSet());
    }
}
