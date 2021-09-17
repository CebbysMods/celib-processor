package lv.cebbys.mcmods.celib.processor.writers;

import lv.cebbys.mcmods.celib.processor.structures.types.ContainerProfile;
import lv.cebbys.mcmods.celib.processor.interfaces.DirectoryProvider;
import lv.cebbys.mcmods.celib.processor.utility.JavaSourceWriter;

import javax.lang.model.element.Element;
import java.util.List;
import java.util.Map;

public class ResourceClassWriter implements DirectoryProvider {
    public void write(DirectoryProvider provider, List<? extends Element> fields, Map<String, String> namedMethods, Map<String, Object> options) {
        try {
            ContainerProfile container = new ContainerProfile().setProfile(null);
            new ResourceFieldWriter().write(container, fields, namedMethods);

            try (JavaSourceWriter writer = JavaSourceWriter.of(provider, container)) {
                writer.append(container.toString(0));
                writer.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
