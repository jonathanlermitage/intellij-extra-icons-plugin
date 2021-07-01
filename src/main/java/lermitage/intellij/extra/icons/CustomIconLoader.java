// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.Base64;
import com.intellij.util.IconUtil;
import com.intellij.util.ImageLoader;
import com.intellij.util.RetinaImage;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class CustomIconLoader {

    private static final ThreadLocal<Boolean> contextUpdated = ThreadLocal.withInitial(() -> false);

    private static final Logger LOGGER = Logger.getInstance(CustomIconLoader.class);

    public static Icon getIcon(Model model, Double additionalUIScale) {
        if (model.getIconType() == IconType.PATH) {
            return IconLoader.getIcon(model.getIcon(), CustomIconLoader.class);
        }
        if (model.getIconType() == IconType.ICON) {
            return model.getIntelliJIcon();
        }
        ImageWrapper fromBase64 = fromBase64(model.getIcon(), model.getIconType(), additionalUIScale);
        if (fromBase64 == null) {
            return null;
        }
        return IconUtil.createImageIcon(fromBase64.getImage());
    }

    /** Load graphics libraries (TwelveMonkeys) in order to make the JVM able to manipulate SVG files. */
    private synchronized static void enhanceImageIOCapabilities() {
        if (!contextUpdated.get()) {
            Thread.currentThread().setContextClassLoader(CustomIconLoader.class.getClassLoader());
            ImageIO.scanForPlugins();
            contextUpdated.set(true);
            LOGGER.info("ImageIO plugins updated with TwelveMonkeys capabilities");
        }
    }

    public static ImageWrapper loadFromVirtualFile(VirtualFile virtualFile) throws IllegalArgumentException {
        enhanceImageIOCapabilities();

        if (virtualFile.getExtension() != null) {
            Image image;
            IconType iconType;
            byte[] fileContents;
            try {
                fileContents = virtualFile.contentsToByteArray();
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileContents);
                if (virtualFile.getExtension().equals("svg") && new String(fileContents).startsWith("<")) {
                    iconType = IconType.SVG;
                } else {
                    iconType = IconType.IMG;
                }
                image = ImageIO.read(byteArrayInputStream);
            } catch (IOException ex) {
                throw new IllegalArgumentException("IOException while trying to load image.");
            }
            if (image == null) {
                throw new IllegalArgumentException("Could not load image properly.");
            }
            return new ImageWrapper(iconType, scaleImage(image, iconType == IconType.SVG), fileContents);
        }
        return null;
    }

    public static ImageWrapper fromBase64(String base64, IconType iconType, double additionalUIScale) {
        enhanceImageIOCapabilities();

        byte[] decodedBase64 = Base64.decode(base64);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decodedBase64);
        Image image;
        try {
            image = ImageIO.read(byteArrayInputStream);
        } catch (IOException ex) {
            LOGGER.info("Can't load " + iconType + " icon: " + ex.getMessage(), ex);
            return null;
        }
        if (image == null) {
            return null;
        }
        Image scaledImage = scaleImage(image, iconType == IconType.SVG);
        if (additionalUIScale != 1f) {
            scaledImage = ImageLoader.scaleImage(scaledImage, additionalUIScale);
        }
        return new ImageWrapper(iconType, scaledImage, decodedBase64);
    }

    public static String toBase64(ImageWrapper imageWrapper) {
        String base64 = null;
        IconType iconType = imageWrapper.getIconType();
        switch (iconType) {
            case SVG:
            case IMG:
                base64 = Base64.encode(imageWrapper.getImageAsByteArray());
                break;
        }
        return base64;
    }

    private static Image scaleImage(Image image, boolean isSVG) {
        int width = image.getWidth(null);
        int height = image.getHeight(null);

        if (isSVG) { // generate high-quality thumbnail
            Image scaledImage = ImageLoader.scaleImage(image, 128, 128);
            return RetinaImage.createFrom(scaledImage, 8f, null);
        }

        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Width or height are unknown.");
        }

        if (width == 16 && height == 16) {
            return image;
        }

        if (width == 32 && height == 32) {
            return RetinaImage.createFrom(image);
        }

        float widthToScaleTo = 16f;
        boolean retina = false;

        if (width >= 32 || height >= 32) {
            widthToScaleTo = 32f;
            retina = true;
        }

        Image scaledImage = ImageLoader.scaleImage(image, widthToScaleTo / Math.max(width, height));
        if (retina) {
            return RetinaImage.createFrom(scaledImage);
        }
        return scaledImage;
    }

    public static class ImageWrapper {

        private final IconType iconType;
        private final Image image;
        private final byte[] imageAsByteArray;

        public ImageWrapper(IconType iconType, Image image, byte[] imageAsByteArray) {
            this.iconType = iconType;
            this.image = image;
            this.imageAsByteArray = imageAsByteArray;
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
    }
}
