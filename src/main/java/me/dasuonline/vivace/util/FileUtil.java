package me.dasuonline.vivace.util;

import java.io.File;
import java.io.IOException;

public class FileUtil {

    public static File checkAndCreateFile(File file) throws IOException {
        if (!file.exists() && file.isFile()) file.createNewFile();
        return file;
    }

    public static File checkAndCreateFolder(File folder) {
        if (!folder.exists()) folder.mkdir();
        return folder;
    }
}
