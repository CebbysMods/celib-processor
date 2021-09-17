package lv.cebbys.mcmods.celib.processor.interfaces;

import lv.cebbys.mcmods.celib.processor.structures.types.ClassProfile;

import java.util.Set;

public interface ImportProvider {
    Set<ClassProfile<?>> getImports();
}
