package lv.cebbys.mcmods.celib.processor.processors;

import com.google.gson.JsonObject;
import lv.cebbys.mcmods.celib.processor.structures.customized.ResourceNamedConsumerListMethodsAndFields;
import lv.cebbys.mcmods.celib.processor.structures.customized.clazz.ClassProfileParser;
import lv.cebbys.mcmods.celib.processor.structures.customized.method.ResourceParseResourceMethods;
import lv.cebbys.mcmods.celib.processor.structures.types.ClassProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.ContainerProfile;

import javax.lang.model.element.Element;

public class MapResourceFieldProcessor {
    public void process(Element field,
                        JsonObject mapResources,
                        ContainerProfile resourceContainer,
                        ContainerProfile namedContainer) {

        ClassProfile<?> resourceProfile = resourceContainer.getProfile();
        ClassProfile<?> namedProfile = namedContainer.getProfile();


        if(mapResources.has("mapping")) {
            JsonObject mapping = mapResources.get("mapping").getAsJsonObject();
            ClassProfile<?> inputClass = ClassProfileParser.of(mapping.get("fullName").getAsString());
            resourceContainer.addMethodsAndFields(
                    ResourceNamedConsumerListMethodsAndFields.jsonMapper(field, resourceProfile, inputClass)
            );
        } else {
            resourceContainer.addMethodsAndFields(ResourceNamedConsumerListMethodsAndFields.of(
                    field, resourceProfile, namedProfile, resourceProfile
            ));
        }
    }
}
