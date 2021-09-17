package lv.cebbys.mcmods.celib.processor.interfaces;

import lv.cebbys.mcmods.celib.processor.structures.types.MethodProfile;

import java.util.LinkedHashSet;

public interface MethodsProvider {
    LinkedHashSet<MethodProfile<?>> getMethods();
}
