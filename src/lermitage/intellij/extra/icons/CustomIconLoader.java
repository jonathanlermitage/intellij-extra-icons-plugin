package lermitage.intellij.extra.icons;

import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CustomIconLoader {

    public static Icon getIcon(Model model) {
        if (model.getIconType() == IconType.PATH) {
            return IconLoader.getIcon(model.getIcon());
        }
        if (model.getIconType() == IconType.ICON) {
            return model.getIntelliJIcon();
        }
        return IconUtil.createImageIcon(fromBase64(model.getIcon(), model.getIconType()).getImage());
    }

    public static ImageWrapper loadFromVirtualFile(VirtualFile virtualFile) throws IllegalArgumentException {
        if (virtualFile.getExtension() != null) {
            Image image;
            IconType iconType;
            byte[] fileContents;
            try {
                fileContents = virtualFile.contentsToByteArray();
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileContents);
                if (virtualFile.getExtension().equals("svg") && new String(fileContents).startsWith("<")) {
                    iconType = IconType.SVG;
                    image = SVGLoader.load(byteArrayInputStream, 1.0f);
                } else {
                    iconType = IconType.IMG;
                    image = ImageLoader.loadFromStream(byteArrayInputStream);
                }
            } catch (IOException ex) {
                throw new IllegalArgumentException("IOException while trying to load image.");
            }
            if (image == null) {
                throw new IllegalArgumentException("Could not load image properly.");
            }

            return new ImageWrapper(iconType, scaleImage(image), fileContents);
        }
        return null;
    }

    public static ImageWrapper fromBase64(String base64, IconType iconType) {
        byte[] decodedBase64 = Base64.decode(base64);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decodedBase64);
        Image image = null;
        try {
            switch (iconType) {
                case SVG:
                    image = SVGLoader.load(byteArrayInputStream, 1.0f);
                    break;
                case IMG:
                    image = ImageLoader.loadFromStream(byteArrayInputStream);
                    break;
            }
        }
        catch (IOException ex) {
            return null;
        }
        if (image == null) {
            return null;
        }
        return new ImageWrapper(iconType, scaleImage(image), decodedBase64);
    }

    public static String toBase64(ImageWrapper imageWrapper) {
        String base64 = null;
        switch (imageWrapper.getIconType()) {
            case SVG:
                base64 = Base64.encode(imageWrapper.getImageAsByteArray());
                break;
            case IMG:
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                try {
                    Image image = imageWrapper.getImage();
                    if (image instanceof JBHiDPIScaledImage) {
                        image = ((JBHiDPIScaledImage) image).getDelegate();
                    }
                    ImageIO.write((BufferedImage) image, "png", outputStream);
                }
                catch (IOException ignored) {

                }
                base64 = Base64.encode(outputStream.toByteArray());
                break;
        }
        return base64;
    }

    private static Image scaleImage(Image image) {
        int width = image.getWidth(null);
        int height = image.getHeight(null);

        if (width != height) {
            throw new IllegalArgumentException("Image should be square.");
        }

        if (width <= 0) {
            throw new IllegalArgumentException("Width and height are unknown.");
        }

        if (width == 16) {
            return image;
        }

        if (width == 32) {
            return RetinaImage.createFrom(image);
        }

        float widthToScaleTo = 16f;
        boolean retina = false;

        if (width >= 32) {
            widthToScaleTo = 32f;
            retina = true;
        }

        Image scaledImage = ImageLoader.scaleImage(image, widthToScaleTo / width);

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
