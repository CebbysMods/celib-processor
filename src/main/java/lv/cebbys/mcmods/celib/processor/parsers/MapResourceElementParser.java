package lv.cebbys.mcmods.celib.processor.parsers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import lv.cebbys.mcmods.celib.processor.utility.DefinedName;
import lv.cebbys.mcmods.celib.processor.exception.ValidationException;
import lv.cebbys.mcmods.celib.processor.utility.Constants;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import java.util.List;
import java.util.Map;

public class MapResourceElementParser {

    private static final String DEFINED_NAME_FULL = DefinedName.class.getName();
    private static final String JSON_FIELD_NAME = DefinedName.class.getDeclaredMethods()[1].getName();
    private static final String METHOD_FIELD_NAME = DefinedName.class.getDeclaredMethods()[0].getName();
    private static final String VALIDATION_REGEX = String.format(
            "^((@%s)(\\(%s=\\\"%s\\\"(, %s=\\\"%s\\\")?\\)))$",
            DEFINED_NAME_FULL, JSON_FIELD_NAME, Constants.JSON_NAME_REGEX,
            METHOD_FIELD_NAME, Constants.METHOD_NAME_REGEX);

    public void parse(AnnotationMirror mirror) {

    }

    @SuppressWarnings("unchecked")
    public Map<String, String> extractMethodNames(AnnotationMirror mirror) throws ValidationException {
        DefinedNamesParser parser = new DefinedNamesParser();
        Map<String, String> mapped = new LinkedTreeMap<>();
        for (ExecutableElement method : mirror.getElementValues().keySet()) {
            if ("definedNames()".equals(method.toString())) {
                List<Object> defines = (List<Object>) mirror.getElementValues().get(method).getValue();
                for (Object define : defines) {
                    JsonObject json = (JsonObject) JsonParser.parseString(parseCompoundString(define.toString()));
                    if (!json.has(METHOD_FIELD_NAME)) {
                        json.addProperty(METHOD_FIELD_NAME, parser.parse(json.get(JSON_FIELD_NAME).getAsString()));
                    }
                    mapped.put(
                            json.get(METHOD_FIELD_NAME).getAsString(),
                            json.get(JSON_FIELD_NAME).getAsString()
                    );
                }
            }
        }
        return mapped;
    }

    public String parseCompoundString(String str) throws ValidationException {
        validateString(str);
        return str.replaceAll(DEFINED_NAME_FULL, "")
                .replaceAll("@", "")
                .replaceAll("\\(", "{")
                .replaceAll("\\)", "}");
    }

    public void validateString(String str) throws ValidationException {
        if (!((str != null) && str.matches(VALIDATION_REGEX))) {
            throw new ValidationException("Passed string does not match DefinedName annotation regex");
        }
    }

}
