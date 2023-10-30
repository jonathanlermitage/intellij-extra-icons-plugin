// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.utils;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.NewUI;
import com.intellij.util.IconUtil;
import com.intellij.util.ui.ImageUtil;
import lermitage.intellij.extra.icons.IconType;
import lermitage.intellij.extra.icons.Model;
import lermitage.intellij.extra.icons.UITypeIconsPreference;
import lermitage.intellij.extra.icons.cfg.services.SettingsService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static lermitage.intellij.extra.icons.utils.Base64Utils.B64_DECODER;
import static lermitage.intellij.extra.icons.utils.Base64Utils.B64_ENCODER;

public class IconUtils {

    private static final @NonNls Logger LOGGER = Logger.getInstance(IconUtils.class);

    private static final int SCALING_SIZE = 16;

    private static final OS DETECT_OS = OS.detectOS();

    private static final File TMP_FOLDER = new File(System.getProperty("java.io.tmpdir"));

    private static final String IDE_INSTANCE_UNIQUE_ID = UUID.randomUUID().toString();

    public static Icon getIcon(Model model, double additionalUIScale, @NotNull UITypeIconsPreference uiTypeIconsPreference) {
        if (model.getIconType() == IconType.PATH) {
            String iconPathToLoad = getIconPathToLoad(model, uiTypeIconsPreference);
            return IconLoader.getIcon(iconPathToLoad, IconUtils.class);
        }
        ImageWrapper fromBase64 = fromBase64(model.getIcon(), model.getIconType(), additionalUIScale);
        if (fromBase64 == null) {
            return null;
        }
        return IconUtil.createImageIcon(fromBase64.getImage());
    }

    private static String getIconPathToLoad(Model model, @NotNull UITypeIconsPreference uiTypeIconsPreference) {
        boolean preferNewUI = switch (uiTypeIconsPreference) {
            case BASED_ON_ACTIVE_UI_TYPE -> NewUI.isEnabled();
            case PREFER_NEW_UI_ICONS -> true;
            case PREFER_OLD_UI_ICONS -> false;
        };
        String iconPathToLoad;
        if (preferNewUI && model.isAutoLoadNewUIIconVariant()) {
            iconPathToLoad = model.getIcon().replace("extra-icons/", "extra-icons/newui/"); //NON-NLS
        } else {
            iconPathToLoad = model.getIcon();
        }
        return iconPathToLoad;
    }

    public static ImageWrapper loadFromVirtualFile(VirtualFile virtualFile) throws IllegalArgumentException {
        if (virtualFile.getExtension() != null) {
            IconType iconType;
            byte[] imageBytes;
            try {
                imageBytes = virtualFile.contentsToByteArray();
                if (virtualFile.getExtension().equals("svg") && new String(imageBytes).startsWith("<")) {
                    iconType = IconType.SVG;
                } else {
                    iconType = IconType.IMG;
                }
                return loadImage(imageBytes, iconType, SettingsService.DEFAULT_ADDITIONAL_UI_SCALE);
            } catch (IOException ex) {
                throw new IllegalArgumentException("IOException while trying to load image.", ex);
            }
        }
        return null;
    }

    public static ImageWrapper fromBase64(String base64, IconType iconType, double additionalUIScale) {
        return loadImage(B64_DECODER.decode(base64), iconType, additionalUIScale);
    }

    /**
     * Creates or retrieves a temporary SVG file based on the provided image bytes.
     * If the SVG file already exists, it is returned. Otherwise, a new file is created
     * with the SHA1 of specified image bytes and returned. The temporary file is deleted
     * when the virtual machine terminates.
     * @param imageBytes the bytes of the image.
     * @return the temporary SVG file.
     * @throws IOException if an I/O error occurs while creating or accessing the file.
     */
    public static synchronized File createOrGetTempSVGFile(byte[] imageBytes) throws IOException {
        File svgFile = new File(TMP_FOLDER, "extra-icons-user-icon" + IDE_INSTANCE_UNIQUE_ID + "-" + DigestUtils.sha1Hex(imageBytes) + ".svg");
        if (!svgFile.exists()) {
            svgFile.deleteOnExit();
            FileUtils.writeByteArrayToFile(svgFile, imageBytes);
        }
        return svgFile;
    }

    private static ImageWrapper loadSVGAsImageWrapper(byte[] imageBytes, double additionalUIScale) {
        String svgFilePath = null;
        try {
            File svgFile = createOrGetTempSVGFile(imageBytes);
            svgFilePath = svgFile.getAbsolutePath();
            String prefix = DETECT_OS == OS.WIN ? "file:/" : "file://"; //NON-NLS // TODO see if should use VfsUtil.fixURLforIDEA(urlStr)
            Icon icon = IconLoader.getIcon(prefix + svgFile.getAbsolutePath().replaceAll("\\\\", "/"), IconUtils.class);
            if (icon.getIconWidth() == 16) {
                return new ImageWrapper(IconType.SVG, IconUtil.toImage(icon), imageBytes);
            } else if (additionalUIScale == 1 || additionalUIScale == 2) {
                Image image = IconUtil.toImage(icon);
                image = ImageUtil.scaleImage(image, (int) (SCALING_SIZE * additionalUIScale), (int) (SCALING_SIZE * additionalUIScale));
                image = scaleImage(image, SCALING_SIZE);
                return new ImageWrapper(IconType.SVG, image, imageBytes);
            } else {
                Image image = IconUtil.toImage(icon);
                image = ImageUtil.scaleImage(image, (int) (SCALING_SIZE * additionalUIScale), (int) (SCALING_SIZE * additionalUIScale));
                return new ImageWrapper(IconType.SVG, image, imageBytes);
            }
        } catch (Exception e) {
            // avoid error report: com.intellij.openapi.application.rw.ReadCancellationException
            // java.lang.Throwable: Control-flow exceptions (e.g. this class com.intellij.openapi.progress.CeProcessCanceledException) should
            // never be logged. Instead, these should have been rethrown if caught.
            String errorName = e.getClass().getName();
            if (errorName.contains("ReadCancellationException") || errorName.contains("ProcessCanceledException")) {
                LOGGER.warn("Can't load an SVG user icon (path: " + svgFilePath + "): " + e.getMessage());
                return null;
            }
            LOGGER.error(e);
            return null;
        }
    }

    public static ImageWrapper loadImage(byte[] imageBytes, IconType iconType, double additionalUIScale) {
        if (iconType == IconType.SVG) {
            try {
                return loadSVGAsImageWrapper(imageBytes, additionalUIScale);
            } catch (Exception ex) {
                LOGGER.info("Can't load " + iconType + " icon: " + ex.getMessage(), ex);
                return null;
            }
        }

        Image image;
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes)) {
            image = ImageIO.read(byteArrayInputStream);
        } catch (IOException ex) {
            LOGGER.info("Can't load " + iconType + " icon: " + ex.getMessage(), ex);
            return null;
        }
        if (image == null) {
            return null;
        }
        Image scaledImage = scaleImage(image, (int) (SCALING_SIZE * additionalUIScale));
        return new ImageWrapper(iconType, scaledImage, imageBytes);
    }

    public static String toBase64(ImageWrapper imageWrapper) {
        String base64 = null;
        IconType iconType = imageWrapper.getIconType();
        switch (iconType) {
            case SVG, IMG -> base64 = B64_ENCODER.encodeToString(imageWrapper.getImageAsByteArray());
        }
        return base64;
    }

    private static Image scaleImage(Image image, int scaling_size) {
        return ImageUtil.scaleImage(image, scaling_size, scaling_size);
    }

    public static class ImageWrapper {

        private final IconType iconType;
        private final Image image;
        private final byte[] imageAsByteArray;
        private final String imageAsBundledIconRef;

        public ImageWrapper(IconType iconType, Image image, byte[] imageAsByteArray) {
            this.iconType = iconType;
            this.image = image;
            this.imageAsByteArray = imageAsByteArray;
            this.imageAsBundledIconRef = null;
        }

        public ImageWrapper(String imageAsBundledIconRef) {
            this.iconType = IconType.PATH;
            this.image = null;
            this.imageAsByteArray = new byte[0];
            this.imageAsBundledIconRef = imageAsBundledIconRef;
        }

        public IconType getIconType() {
            return iconType;
        }

        public Image getImage() {
            return image;
        }

        public byte[] getImageAsByteArray() {
            return imageAsByteArray;
        }

        public String getImageAsBundledIconRef() {
            return imageAsBundledIconRef;
        }
    }
}
