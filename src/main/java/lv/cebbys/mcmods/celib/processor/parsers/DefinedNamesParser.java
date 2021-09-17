package lv.cebbys.mcmods.celib.processor.parsers;

import lv.cebbys.mcmods.celib.processor.exception.ValidationException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DefinedNamesParser {

    private static final String DEFAULT_REGEX = "^([a-zA-Z\\d]+)$";
    private static final String COMPLEX_REGEX = "^([\\s\\w\\d\\.\\/\\-\\#]+)$";
    private static final String NAME_SPLITTER_REGEX = "([\\s\\.\\/\\-\\_\\#]+)";

    Map<String, String> methodsAndNames = new HashMap<>();

    public Map<String, String> results() {
        return methodsAndNames;
    }

    public String parse(String name) throws ValidationException {
        if (name != null) {
            if (name.matches(DEFAULT_REGEX)) {
                return name;
            } else if (name.matches(COMPLEX_REGEX)) {
                String methodName = name.replaceAll(NAME_SPLITTER_REGEX, "#");
                while (methodName.contains("#")) {
                    int index = methodName.indexOf("#");
                    String start = methodName.substring(0, index);
                    if(index != methodName.length() - 1) {
                        String end = methodName.substring(index + 1);
                        start = start + end.substring(0, 1).toUpperCase(Locale.ROOT) + end.substring(1);
                    }
                    methodName = start;
                }
                return methodName;
            }
        }
        throw new ValidationException("Invalid string passed to DefinedNamesParser");
    }

}
