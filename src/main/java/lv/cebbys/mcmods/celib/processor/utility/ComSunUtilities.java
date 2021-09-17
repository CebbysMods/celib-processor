package lv.cebbys.mcmods.celib.processor.utility;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import java.util.List;

@SuppressWarnings("unchecked")
public class ComSunUtilities {
    public static final String COM_SUN_ARRAY = "com.sun.tools.javac.code.Attribute$Array";
    public static final String COM_SUN_LIST = "com.sun.tools.javac.util.List";
    public static final String COM_SUN_COMPOUND = "com.sun.tools.javac.code.Attribute$Compound";
    public static final String COM_SUN_CONSTANT = "com.sun.tools.javac.code.Attribute$Constant";
    public static final String COM_SUN_CLASS = "com.sun.tools.javac.code.Attribute$Class";
    public static final String COM_SUN_CLASS_TYPE = "com.sun.tools.javac.code.Type$ClassType";

    public static boolean isArray(Object o) {
        return COM_SUN_ARRAY.equals(o.getClass().getName());
    }

    public static boolean isList(Object o) {
        return COM_SUN_LIST.equals(o.getClass().getName());
    }

    public static boolean isCompound(Object o) {
        return COM_SUN_COMPOUND.equals(o.getClass().getName());
    }

    public static boolean isConstant(Object o) {
        return COM_SUN_CONSTANT.equals(o.getClass().getName());
    }

    public static boolean isClass(Object o) {
        return COM_SUN_CLASS.equals(o.getClass().getName());
    }

    public static boolean isClassType(Object o) {
        return COM_SUN_CLASS_TYPE.equals(o.getClass().getName());
    }

    public static List<AnnotationValue> getList(Object o) {
        return (List<AnnotationValue>) o;
    }

    public static AnnotationMirror getAnnotationMirror(Object o) {
        return (AnnotationMirror) o;
    }

    public static AnnotationValue getAnnotationValue(Object o) {
        return (AnnotationValue) o;
    }

    public static DeclaredType getClassType(Object o) {
        return (DeclaredType) o;
    }
    public static Element getClassTypeElement(Object o) {
        return ((DeclaredType) o).asElement();
    }
}
