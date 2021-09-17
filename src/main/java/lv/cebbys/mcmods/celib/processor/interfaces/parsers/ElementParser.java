package lv.cebbys.mcmods.celib.processor.interfaces.parsers;

import javax.lang.model.element.Element;
import java.util.List;

public interface ElementParser {
    default void parseElement(Element element, ElementParserFunction parsed) {
        String fullName = element.asType().toString();
        String className = fullName.substring(fullName.lastIndexOf(".") + 1);
        String packageName = fullName.substring(0, fullName.lastIndexOf("."));
        List<? extends Element> elements = element.getEnclosedElements();
        parsed.function(fullName, className, packageName, elements);
    }

    interface ElementParserFunction {
        void function(String fullName, String className, String packageName, List<? extends Element> childFields);
    }
}
