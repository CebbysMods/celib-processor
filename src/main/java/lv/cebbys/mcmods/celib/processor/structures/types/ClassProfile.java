package lv.cebbys.mcmods.celib.processor.structures.types;

import lv.cebbys.mcmods.celib.processor.interfaces.ImportProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public abstract class ClassProfile<T> implements Cloneable, ImportProvider {

    protected T instance;
    protected String className;
    protected String packageName;
    protected ClassProfile<?> extendClass;
    protected List<ClassProfile<?>> implementClass = new ArrayList<>();

    public abstract ClassProfile<?> clone();

    @Override
    public Set<ClassProfile<?>> getImports() {
        Set<ClassProfile<?>> set = new HashSet<>();
        if(extendClass != null) {
            set.add(extendClass);
        }
        if(implementClass.size() > 0) {
            set.addAll(implementClass);
        }
        return set;
    }

    protected T setInstance(T i) {
        instance = i;
        return instance;
    }

    public T setClassName(String n) {
        className = n;
        return instance;
    }

    public T setClassName(Class<?> n) {
        className = n.getSimpleName();
        return instance;
    }

    public T setPackageName(String n) {
        packageName = n;
        return instance;
    }

    public T setPackageName(Class<?> n) {
        packageName = n.getPackageName();
        return instance;
    }

    public T setExtend(ClassProfile<?> e) {
        extendClass = e;
        return instance;
    }

    public T setImplements(Consumer<List<ClassProfile<?>>> consumer) {
        List<ClassProfile<?>> list = new ArrayList<>();
        consumer.accept(list);
        implementClass.clear();
        implementClass.addAll(list);
        return instance;
    }

    public T addImplement(ClassProfile<?> i) {
        implementClass.add(i);
        return instance;
    }


    public String getClassName() {
        return className;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getFullName() {
        return getPackageName() + "." + getClassName();
    }

    public ClassProfile<?> getExtend() {
        return extendClass;
    }

    public List<ClassProfile<?>> getImplemented() {
        return List.copyOf(implementClass);
    }

    public boolean isArray() {
        return className.contains("[]");
    }

    public String importString() {
        return getFullName();
    }
    public String parameterString() {
        return getClassName();
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof ClassProfile<?> p) {
            return getFullName().equals(p.getFullName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = className != null ? className.hashCode() : 0;
        result = 31 * result + (packageName != null ? packageName.hashCode() : 0);
        result = 31 * result + (extendClass != null ? extendClass.hashCode() : 0);
        result = 31 * result + (implementClass != null ? implementClass.hashCode() : 0);
        return result;
    }

}
