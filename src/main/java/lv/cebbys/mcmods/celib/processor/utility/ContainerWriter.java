package lv.cebbys.mcmods.celib.processor.utility;

import lv.cebbys.mcmods.celib.processor.interfaces.DirectoryProvider;
import lv.cebbys.mcmods.celib.processor.structures.types.ContainerProfile;

import java.io.IOException;

public class ContainerWriter implements DirectoryProvider {
    public static final ContainerWriter INSTANCE = new ContainerWriter();

    private ContainerWriter() {}

    public void write(ContainerProfile container) {
        try(JavaSourceWriter writer = JavaSourceWriter.of(this, container)) {
            writer.append(container.toString(0));
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
