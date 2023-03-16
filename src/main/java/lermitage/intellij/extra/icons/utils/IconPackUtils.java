// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lermitage.intellij.extra.icons.cfg.IconPack;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class IconPackUtils {

    public static final Charset CHARSET = StandardCharsets.UTF_8;

    private static final Gson gson = new GsonBuilder()
        .disableHtmlEscaping()
        //.setPrettyPrinting()
        //.serializeNulls()
        .create();

    /**
     * Read an icon pack from a JSON file.
     *
     * @param file icon pack file.
     * @return icon pack.
     * @throws IOException if any error occurs reading the local file.
     */
    public static IconPack fromJsonFile(File file) throws IOException {
        String modelsAsJson = FileUtils.readFileToString(file, CHARSET);
        return toIconPack(modelsAsJson);
    }

    private static IconPack toIconPack(String modelsAsJson) {
        return gson.fromJson(modelsAsJson, IconPack.class);
    }

    /**
     * Export an icon pack to a JSON file.
     *
     * @param file icon pack file.
     * @param iconPack icon pack.
     * @throws IOException if any error occurs writing the local file.
     */
    public static void writeToJsonFile(File file, IconPack iconPack) throws IOException {
        String json = gson.toJson(iconPack, IconPack.class);
        FileUtils.writeStringToFile(file, json, IconPackUtils.CHARSET);
    }
}
