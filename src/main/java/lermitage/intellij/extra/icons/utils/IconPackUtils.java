// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lermitage.intellij.extra.icons.cfg.IconPack;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class IconPackUtils {

    private static final Charset CHARSET = StandardCharsets.UTF_8;

    private static final Gson gson = new GsonBuilder()
        .disableHtmlEscaping()
        .create();

    /**
     * Read an icon pack from a JSON file.
     * @param file icon pack file.
     * @return icon pack.
     * @throws IOException if any error occurs reading the local file.
     */
    public static IconPack fromJsonFile(File file) throws IOException {
        String modelsAsJson = Files.readString(file.toPath(), CHARSET);
        return gson.fromJson(modelsAsJson, IconPack.class);
    }

    /**
     * Export an icon pack to a JSON file.
     * @param file icon pack file.
     * @param iconPack icon pack.
     * @throws IOException if any error occurs writing the local file.
     */
    public static void writeToJsonFile(File file, IconPack iconPack) throws IOException {
        String json = gson.toJson(iconPack, IconPack.class);
        Files.writeString(file.toPath(), json, CHARSET);
    }
}
