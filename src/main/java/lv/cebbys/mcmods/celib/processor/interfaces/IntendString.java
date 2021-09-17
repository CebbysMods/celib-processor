package lv.cebbys.mcmods.celib.processor.interfaces;

public interface IntendString {
    String toString(int intends);

    default StringBuilder intend(StringBuilder builder, int intends) {
        return builder.append("\t".repeat(intends));
    }
}
