package lv.cebbys.mcmods.celib.processor.processors;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lv.cebbys.mcmods.celib.processor.api.Named;
import lv.cebbys.mcmods.celib.processor.api.Resource;
import lv.cebbys.mcmods.celib.processor.api.elements.ArrayResourceElement;
import lv.cebbys.mcmods.celib.processor.api.elements.MapResourceElement;
import lv.cebbys.mcmods.celib.processor.api.elements.ResourceElement;
import lv.cebbys.mcmods.celib.processor.interfaces.parsers.AnnotationMirrorConverter;
import lv.cebbys.mcmods.celib.processor.interfaces.parsers.AnnotationParser;
import lv.cebbys.mcmods.celib.processor.interfaces.parsers.ElementParser;
import lv.cebbys.mcmods.celib.processor.interfaces.parsers.FieldParser;
import lv.cebbys.mcmods.celib.processor.structures.customized.NamedDefaultMethodsAndFields;
import lv.cebbys.mcmods.celib.processor.structures.customized.ResourceArrayMethodsAndFields;
import lv.cebbys.mcmods.celib.processor.structures.customized.ResourceDefaultMethodsAndFields;
import lv.cebbys.mcmods.celib.processor.structures.customized.ResourceNamedConsumerListMethodsAndFields;
import lv.cebbys.mcmods.celib.processor.structures.customized.clazz.ClassProfileParser;
import lv.cebbys.mcmods.celib.processor.structures.customized.method.NamedExtendedMethods;
import lv.cebbys.mcmods.celib.processor.structures.customized.method.ResourceParseResourceMethods;
import lv.cebbys.mcmods.celib.processor.structures.customized.method.ResourceToJsonMethods;
import lv.cebbys.mcmods.celib.processor.structures.types.ClassProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.ContainerProfile;
import lv.cebbys.mcmods.celib.processor.structures.types.customized.clazz.BasicClassProfile;
import lv.cebbys.mcmods.celib.processor.utility.ContainerWriter;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.LinkedHashSet;
import java.util.Set;

@SupportedAnnotationTypes({"lv.cebbys.mcmods.celib.processor.api.Resource" })
@SupportedSourceVersion(SourceVersion.RELEASE_16)
public class ResourceProcessor extends AbstractProcessor
        implements AnnotationParser, ElementParser, FieldParser, AnnotationMirrorConverter {

    private final MapResourceFieldProcessor mapResourceFieldProcessor = new MapResourceFieldProcessor();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        parseAnnotations(annotations, roundEnv, (element) -> parseElement(element, (full, name, pack, fields) -> {
            ClassProfile<?> resourceProfile = resourceProfile(pack, name);
            ClassProfile<?> namedProfile = namedProfile(pack, name);

            ContainerProfile resourceContainer = new ContainerProfile().setProfile(resourceProfile);
            ContainerProfile namedContainer = new ContainerProfile().setProfile(namedProfile);

            namedContainer.addMethodsAndFields(new NamedDefaultMethodsAndFields(resourceProfile));

            JsonObject namedAnnotation = jsonFromAnnotationMirrors(element.getAnnotationMirrors(), Named.class);
            if (namedAnnotation != null) {
                namedContainer.addMethods(new NamedExtendedMethods(resourceProfile, namedAnnotation));
            }

//            JsonObject resourceAnnotation = jsonFromAnnotationMirrors(element.getAnnotationMirrors(), Resource.class);
//            if (resourceAnnotation != null && resourceAnnotation.has("jsonParser")) {
//                if (resourceAnnotation.get("jsonParser").getAsJsonObject().get("value").getAsBoolean()) {
//                    resourceContainer.addMethods(new ResourceToJsonMethods());
//                }
//            }

            parseFields(fields, (field, fieldAnnotations) -> {
                JsonObject mapResources = jsonFromAnnotationMirrors(fieldAnnotations, MapResourceElement.class);
                LinkedHashSet<JsonElement> arrayResources = convertAnnotationMirrors(fieldAnnotations, ArrayResourceElement.class);
                LinkedHashSet<JsonElement> resources = convertAnnotationMirrors(fieldAnnotations, ResourceElement.class);

                if (mapResources != null) {
                    mapResourceFieldProcessor.process(field, mapResources, resourceContainer, namedContainer);
                }


                if (arrayResources.size() > 0) {
                    resourceContainer.addMethodsAndFields(new ResourceArrayMethodsAndFields(field, resourceProfile, arrayResources));
                }


                if (resources.size() > 0) {
                    resourceContainer.addMethodsAndFields(new ResourceDefaultMethodsAndFields(field, resourceProfile));
                }
            });

            ContainerWriter.INSTANCE.write(resourceContainer);
            ContainerWriter.INSTANCE.write(namedContainer);
        }));
        return false;
    }

    private ClassProfile<?> resourceProfile(String pack, String name) {
        return BasicClassProfile.of()
                .setPackageName(pack)
                .setClassName(name + "Resource");
    }

    private ClassProfile<?> namedProfile(String pack, String name) {
        return BasicClassProfile.of()
                .setPackageName(pack)
                .setClassName(name + "Named");
    }
}
