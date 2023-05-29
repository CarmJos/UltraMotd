package cc.carm.plugin.ultramotd.utils;

import cc.carm.lib.easyplugin.utils.JarResourceUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class ConfigFolderUtils {

    public static File initialize(@NotNull File rootFolder, String folderPath) throws IOException {
        File folder = new File(rootFolder, folderPath);
        if (folder.exists() && folder.isDirectory()) return folder;

        if (folder.exists() && !folder.isDirectory()) {
            folder.delete();
        }

        JarResourceUtils.copyFolderFromJar(folderPath, rootFolder, JarResourceUtils.CopyOption.COPY_IF_NOT_EXIST);
        return folder;
    }

}
