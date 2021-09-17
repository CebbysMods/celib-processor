package lv.cebbys.mcmods.celib.processor.structures.types.customized.method;

import lv.cebbys.mcmods.celib.processor.structures.types.ClassProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.FieldProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.MethodProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.clazz.GenericParameterClassProfile;
import lv.cebbys.mcmods.celib.processor.utility.ModifierComparator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GenericMethodProfile extends MethodProfile<GenericMethodProfile> {

    protected Map<String, GenericParameterClassProfile> generics = new HashMap<>();

    private GenericMethodProfile() {

    }

    public static GenericMethodProfile of() {
        GenericMethodProfile profile = new GenericMethodProfile();
        return profile.setInstance(profile);
    }

    public static GenericMethodProfile of(String... varargs) {
        GenericMethodProfile profile = GenericMethodProfile.of();
        return profile.setVarargs(varargs);
    }

    public GenericParameterClassProfile getVararg(String key) {
        return generics.get(key);
    }

    public GenericMethodProfile setVarargs(Consumer<List<String>> varargs) {
        List<String> list = new ArrayList<>();
        varargs.accept(list);
        return setVarargs(list);
    }

    public GenericMethodProfile setVarargs(List<String> varargs) {
        generics.clear();
        varargs.forEach(arg -> {
            generics.put(arg, GenericParameterClassProfile.of().setClassName(arg));
        });
        return this;
    }

    public GenericMethodProfile setVarargs(String... varargs) {
        return setVarargs(Arrays.asList(varargs));
    }

    @Override
    public String toString(int intends) {
        StringBuilder builder = new StringBuilder();
        intend(builder, intends);

        List<String> mods = modifiers.stream().sorted(ModifierComparator.INSTANCE).collect(Collectors.toList());
        for(String mod : mods) {
            builder.append(mod).append(" ");
        }

        builder.append("<");
        boolean first = true;
        for(String key : generics.keySet()) {
            ClassProfile<?> profile = generics.get(key);
            if(first) {
                first = false;
            } else {
                builder.append(", ");
            }
            builder.append(profile.parameterString());
        }
        builder.append("> ");

        builder.append(returnType == null ? "void" : returnType.parameterString()).append(" ");
        builder.append(methodName).append("(");
        first = true;
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
}
