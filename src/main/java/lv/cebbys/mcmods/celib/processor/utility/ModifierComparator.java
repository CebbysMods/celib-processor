package lv.cebbys.mcmods.celib.processor.utility;

import java.util.Arrays;
import java.util.Comparator;

public class ModifierComparator implements Comparator<String> {
    public static ModifierComparator INSTANCE = new ModifierComparator();

    private ModifierComparator() {}

    public static final String[] ACCESS_MODIFIERS = {"public", "private", "protected"};
    public static final String STATIC_MODIFIER = "static";
    public static final String FINAL_MODIFIER = "final";

    @Override
    public int compare(String o1, String o2) {
        if(o1 == null) {
            return o2 == null ? 0 : -1;
        }
        if(o2 == null) {
            return 1;
        }
        int i1 = Arrays.asList(ACCESS_MODIFIERS).contains(o1) ? 2 : STATIC_MODIFIER.equals(o1) ? 1 : 0;
        int i2 = Arrays.asList(ACCESS_MODIFIERS).contains(o2) ? 2 : STATIC_MODIFIER.equals(o2) ? 1 : 0;
        return i1 - i2;
    }
}
