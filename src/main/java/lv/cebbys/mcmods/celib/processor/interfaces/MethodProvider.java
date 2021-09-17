package lv.cebbys.mcmods.celib.processor.interfaces;

import lv.cebbys.mcmods.celib.processor.structures.types.MethodProfile;

public interface MethodProvider {
    MethodProfile<?> getMethod();
}
