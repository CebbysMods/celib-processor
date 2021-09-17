package lv.cebbys.mcmods.celib.processor.interfaces;

import lv.cebbys.mcmods.celib.processor.structures.types.FieldProfile;

import java.util.LinkedHashSet;

public interface FieldsProvider {
    LinkedHashSet<FieldProfile<?>> getFields();
}
