package lv.cebbys.mcmods.celib.processor.utility;

import lv.cebbys.mcmods.celib.processor.structures.types.ContainerProfile;
import lv.cebbys.mcmods.celib.processor.interfaces.DirectoryProvider;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JavaSourceWriter extends BufferedWriter {

    private JavaSourceWriter(File source) throws IOException {
        super(new FileWriter(source, false));
    }

    public static JavaSourceWriter of(DirectoryProvider provider, ContainerProfile profile) throws IOException {
        File source = new File(classPath(provider, profile));
        if(source.exists()) {
            if(!source.delete()) {
                System.out.println("Failed to delete source file: " + source);
            }
        }
        if(!source.getParentFile().exists()) {
            if(!source.getParentFile().mkdirs()) {
                System.out.println("Failed to create source file folder: " + source.getParentFile());
            }
        }
        return new JavaSourceWriter(source);
    }

    private static String classPath(DirectoryProvider provider, ContainerProfile profile) {
        return provider.getRootDirectory() + "\\" +
                profile.getProfile().getFullName().replaceAll("\\.", "\\\\") + ".java";
    }
}
