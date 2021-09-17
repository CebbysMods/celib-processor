package lv.cebbys.mcmods.celib.processor.structures.customized;

import lv.cebbys.mcmods.celib.processor.interfaces.MethodsAndFieldsProvider;
import lv.cebbys.mcmods.celib.processor.structures.types.ClassProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.FieldProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.MethodProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.clazz.ConsumerClassProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.field.BasicFieldProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.method.BasicMethodProfile;

import javax.lang.model.element.Element;
import java.util.Arrays;
import java.util.LinkedHashSet;

public class ResourceDefaultMethodsAndFields implements MethodsAndFieldsProvider {

    private final LinkedHashSet<FieldProfile<?>> fields = new LinkedHashSet<>();
    private final LinkedHashSet<MethodProfile<?>> methods = new LinkedHashSet<>();

    public ResourceDefaultMethodsAndFields(Element element, ClassProfile<?> resource) {
        BasicFieldProfile field = BasicFieldProfile.of(element);
        BasicMethodProfile method = BasicMethodProfile.of()
                .setModifiers("public")
                .setReturnType(resource)
                .setMethodName(field.getName());
        if (isComplex(field)) {
            method.setParameters(BasicFieldProfile.of().setName("consumer")
                    .setType(ConsumerClassProfile.of().setConsumer(field.getType())))
                    .addLine("this.%s = new %s();", field.getName(), field.getType().parameterString())
                    .addLine("consumer.accept(this.%s);", field.getName());
        } else {
            method.setParameters(BasicFieldProfile.of(element).setName("p").setType(field.getType()))
                    .addLine("this.%s = p;", field.getName());
        }
        methods.add(method.addLine("return this;"));
        fields.add(field);
    }

    @Override
    public LinkedHashSet<FieldProfile<?>> getFields() {
        return new LinkedHashSet<>(fields);
    }

    @Override
    public LinkedHashSet<MethodProfile<?>> getMethods() {
        return new LinkedHashSet<>(methods);
    }

    private boolean isComplex(FieldProfile<?> profile) {
        return !Arrays.asList(SIMPLE_TYPES).contains(profile.getType().getFullName());
    }

    private static final String SIMPLE_TYPE_STRING = String.class.getName();
    private static final String SIMPLE_TYPE_INTEGER = Integer.class.getName();
    private static final String SIMPLE_TYPE_BOOLEAN = Boolean.class.getName();
    private static final String[] SIMPLE_TYPES = {
            SIMPLE_TYPE_STRING, SIMPLE_TYPE_INTEGER, SIMPLE_TYPE_BOOLEAN
    };
}
