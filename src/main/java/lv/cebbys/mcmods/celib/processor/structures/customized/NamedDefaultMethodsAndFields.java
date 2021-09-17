package lv.cebbys.mcmods.celib.processor.structures.customized;

import lv.cebbys.mcmods.celib.processor.interfaces.MethodsAndFieldsProvider;
import lv.cebbys.mcmods.celib.processor.structures.customized.method.NamedGetMethod;
import lv.cebbys.mcmods.celib.processor.structures.types.ClassProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.FieldProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.MethodProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.clazz.BasicClassProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.field.BasicFieldProfile;

import java.util.LinkedHashSet;

public class NamedDefaultMethodsAndFields implements MethodsAndFieldsProvider {

    private final LinkedHashSet<MethodProfile<?>> methods = new LinkedHashSet<>();
    private final LinkedHashSet<FieldProfile<?>> fields = new LinkedHashSet<>();

    public NamedDefaultMethodsAndFields(ClassProfile<?> resource) {
        fields.add(BasicFieldProfile.of()
                .setModifiers("private")
                .setType(BasicClassProfile.of(String.class))
                .setName("name")
        );
        fields.add(BasicFieldProfile.of()
                .setModifiers("private")
                .setType(resource)
                .setName("resource")
        );
        methods.add(new NamedGetMethod(resource).getMethod());
    }

    @Override
    public LinkedHashSet<FieldProfile<?>> getFields() {
        return new LinkedHashSet<>(fields);
    }

    @Override
    public LinkedHashSet<MethodProfile<?>> getMethods() {
        return new LinkedHashSet<>(methods);
    }
}
