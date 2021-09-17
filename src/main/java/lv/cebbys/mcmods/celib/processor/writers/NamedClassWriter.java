package lv.cebbys.mcmods.celib.processor.writers;

import lv.cebbys.mcmods.celib.processor.structures.customized.method.NamedGetMethod;
import lv.cebbys.mcmods.celib.processor.structures.types.ContainerProfile;
import lv.cebbys.mcmods.celib.processor.interfaces.DirectoryProvider;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.clazz.BasicClassProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.field.BasicFieldProfile;
import lv.cebbys.mcmods.celib.processor.utility.JavaSourceWriter;

import java.util.Map;

public class NamedClassWriter {
    public void write(DirectoryProvider provider, Map<String, String> additionalMethods) {
        try {
            ContainerProfile container = new ContainerProfile().setProfile(null);

            container.addField(BasicFieldProfile.of()
                    .setModifiers("private")
                    .setType(BasicClassProfile.of(String.class))
                    .setName("name")
            );

            container.addField(BasicFieldProfile.of()
                    .setModifiers("private")
                    .setType(null)
                    .setName("resource")
            );

            container.addMethod(new NamedGetMethod(null));
            for(String methodName : additionalMethods.keySet()) {
                String jsonName = additionalMethods.get(methodName);
                container.addMethod(
                        new NamedGetMethod(
                                null,
                                methodName,
                                jsonName
                        )
                );
            }

            try (JavaSourceWriter writer = JavaSourceWriter.of(provider, container)) {
                writer.append(container.toString(0));
                writer.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
