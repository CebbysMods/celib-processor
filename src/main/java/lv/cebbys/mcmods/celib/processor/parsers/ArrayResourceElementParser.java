package lv.cebbys.mcmods.celib.processor.parsers;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArrayResourceElementParser {

    public List<String> parameters = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public void parse(AnnotationMirror annotation) {
        List<? extends ExecutableElement> keys = new ArrayList<>(annotation.getElementValues().keySet());
        Set<String> keySet = new HashSet<>();
        parameters.clear();
        keys.forEach(key -> {
            if(key != null) {
                String keyString = key.toString();
                keySet.add(keyString);
                if ("elements()".equals(keyString)) {
                    System.out.println(annotation.getElementValues().get(key));


                    List<Object> elements = (List<Object>) annotation.getElementValues().get(key).getValue();
                    elements.forEach(e -> {
                        parameters.add(e.toString().replaceAll("\"", ""));
                    });
                }
            }
        });
    }

    public List<String> getParameterNames() {
        return List.copyOf(parameters);
    }
}
