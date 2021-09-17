package lv.cebbys.mcmods.celib.processor.structures.customized.method;

import lv.cebbys.mcmods.celib.processor.interfaces.MethodProvider;
import lv.cebbys.mcmods.celib.processor.structures.types.ClassProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.MethodProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.clazz.BasicClassProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.field.BasicFieldProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.method.BasicMethodProfile;

public class NamedGetMethod implements MethodProvider {

    private final MethodProfile<?> profile;

    public NamedGetMethod(ClassProfile<?> resource, String methodName, String jsonName) {
        profile = BasicMethodProfile.of()
                .setModifiers("public")
                .setReturnType(resource)
                .setMethodName(methodName)
                .addLine("this.name = \"%s\";", jsonName)
                .addLine("this.resource = new %s();", resource.getClassName())
                .addLine("return this.resource;");
    }

    public NamedGetMethod(ClassProfile<?> resource) {
        profile = BasicMethodProfile.of()
                .setModifiers("public")
                .setReturnType(resource)
                .setMethodName("name")
                .setParameters(BasicFieldProfile.of().setType(BasicClassProfile.of(String.class)).setName("n"))
                .addLine("this.name = %s;", "n")
                .addLine("this.resource = new %s();", resource.getClassName())
                .addLine("return this.resource;");
    }

    @Override
    public MethodProfile<?> getMethod() {
        return profile;
    }
}
