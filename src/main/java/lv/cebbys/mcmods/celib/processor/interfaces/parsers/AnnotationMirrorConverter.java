package lv.cebbys.mcmods.celib.processor.interfaces.parsers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lv.cebbys.mcmods.celib.processor.filters.AnnotationFilter;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static lv.cebbys.mcmods.celib.processor.utility.ComSunUtilities.getAnnotationMirror;
import static lv.cebbys.mcmods.celib.processor.utility.ComSunUtilities.getAnnotationValue;
import static lv.cebbys.mcmods.celib.processor.utility.ComSunUtilities.getClassType;
import static lv.cebbys.mcmods.celib.processor.utility.ComSunUtilities.getClassTypeElement;
import static lv.cebbys.mcmods.celib.processor.utility.ComSunUtilities.getList;
import static lv.cebbys.mcmods.celib.processor.utility.ComSunUtilities.isArray;
import static lv.cebbys.mcmods.celib.processor.utility.ComSunUtilities.isClass;
import static lv.cebbys.mcmods.celib.processor.utility.ComSunUtilities.isClassType;
import static lv.cebbys.mcmods.celib.processor.utility.ComSunUtilities.isCompound;
import static lv.cebbys.mcmods.celib.processor.utility.ComSunUtilities.isConstant;
import static lv.cebbys.mcmods.celib.processor.utility.ComSunUtilities.isList;

public interface AnnotationMirrorConverter {
    default <T extends Annotation> LinkedHashSet<JsonElement> convertAnnotationMirrors(Set<AnnotationMirror> mirrors,
                                                                                       Class<T> annotationType) {
        LinkedHashSet<JsonElement> mapped = new LinkedHashSet<>();
        mirrors.stream().filter(new AnnotationFilter(annotationType)).forEachOrdered(mirror -> {
            mapped.add(recursiveMapping(mirror));
        });
        return mapped;
    }

    default <T extends Annotation> LinkedHashSet<JsonElement> convertAnnotationMirrors(List<? extends AnnotationMirror> mirrors,
                                                                                       Class<T> annotationType) {
        LinkedHashSet<JsonElement> mapped = new LinkedHashSet<>();
        mirrors.stream().filter(new AnnotationFilter(annotationType)).forEachOrdered(mirror -> {
            mapped.add(recursiveMapping(mirror));
        });
        return mapped;
    }

    default <T extends Annotation> JsonObject jsonFromAnnotationMirrors(List<? extends AnnotationMirror> mirrors,
                                                                        Class<T> annotationType) {
        JsonElement element = mirrors.stream()
                .filter(new AnnotationFilter(annotationType))
                .map(this::recursiveMapping)
                .findFirst().orElse(null);
        return element == null ? null : element.getAsJsonObject();
    }

    default <T extends Annotation> JsonObject jsonFromAnnotationMirrors(Set<? extends AnnotationMirror> mirrors,
                                                                        Class<T> annotationType) {
        JsonElement element = mirrors.stream()
                .filter(new AnnotationFilter(annotationType))
                .map(this::recursiveMapping)
                .findFirst().orElse(null);
        return element == null ? null : element.getAsJsonObject();
    }

    private JsonElement recursiveMapping(Object obj) {
        if (isList(obj)) {
            List<AnnotationValue> list = getList(obj);
            JsonArray json = new JsonArray();
            list.forEach(le -> {
                json.add(recursiveMapping(le));
            });
            return json;
        } else if (isCompound(obj)) {
            JsonObject json = new JsonObject();
            AnnotationMirror mirror = getAnnotationMirror(obj);
            mirror.getElementValues().keySet().forEach(key -> {
                json.add(
                        key.toString().replaceAll("\\(\\)", ""),
                        recursiveMapping(mirror.getElementValues().get(key))
                );
            });
            return json;
        } else if (isConstant(obj)) {
            AnnotationValue value = getAnnotationValue(obj);
            JsonObject json = new JsonObject();
            json.addProperty("value", value.getValue().toString());
            json.addProperty("class", value.getValue().getClass().getName());
            return json;
        } else if (isArray(obj)) {
            AnnotationValue value = getAnnotationValue(obj);
            return recursiveMapping(value.getValue());
        } else if(isClass(obj)) {
            AnnotationValue value = getAnnotationValue(obj);
            return recursiveMapping(value.getValue());
        } else if(isClassType(obj)) {
            Element type = getClassTypeElement(obj);
            JsonObject json = new JsonObject();
            json.addProperty("className", type.getSimpleName().toString());
            json.addProperty("fullName", type.toString());
            return json;
        } else {
            throw new RuntimeException(obj.getClass() + " Attribute parsing has not been implemented. Contact the developer");
        }
    }
}
