package lv.cebbys.mcmods.celib.processor.interfaces;

import java.io.File;

public interface DirectoryProvider {
    default File getRootDirectory() {
        try {
            String envPath = System.getProperty("processor_source_dir");
            if(envPath != null) {
                return new File(envPath + "\\src\\main\\generated\\");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
