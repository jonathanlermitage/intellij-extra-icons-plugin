// SPDX-License-Identifier: MIT

package lermitage.intellij.extra.icons.utils;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.IconUtil;
import com.intellij.util.ImageLoader;
import com.intellij.util.ui.ImageUtil;
import com.intellij.util.ui.JBImageIcon;
import lermitage.intellij.extra.icons.IconType;
import lermitage.intellij.extra.icons.Model;
import lermitage.intellij.extra.icons.cfg.services.SettingsService;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.util.SVGConstants;
import org.jetbrains.annotations.NonNls;
import org.w3c.dom.DOMImplementation;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static lermitage.intellij.extra.icons.utils.Base64Utils.B64_DECODER;
import static lermitage.intellij.extra.icons.utils.Base64Utils.B64_ENCODER;

public class IconUtils {

    private static final @NonNls Logger LOGGER = Logger.getInstance(IconUtils.class);

    private static final ThreadLocal<TranscodingHints> localTranscoderHints = ThreadLocal.withInitial(() -> null);
    private static final ThreadLocal<DOMImplementation> localSVGDOMImplementation = ThreadLocal.withInitial(() -> null);
    private static final ThreadLocal<Boolean> localContextUpdated = ThreadLocal.withInitial(() -> false);

    private static final int SCALING_SIZE = 16;
    private static final float SVG_SIZE_BEFORE_RESCALING = 128f;
    private static final Pattern cssVarRe = Pattern.compile("var\\([-\\w]+\\)");

    public static Icon getIcon(Model model, double additionalUIScale) {
        if (model.getIconType() == IconType.PATH) {
            return IconLoader.getIcon(model.getIcon(), IconUtils.class);
        }
        ImageWrapper fromBase64 = fromBase64(model.getIcon(), model.getIconType(), additionalUIScale);
        if (fromBase64 == null) {
            return null;
        }
        return IconUtil.createImageIcon(fromBase64.getImage());
    }

    /**
     * Load graphics libraries (TwelveMonkeys) in order to make the JVM able to manipulate SVG files.
     */
    private synchronized static void enhanceImageIOCapabilities() {
        if (!localContextUpdated.get()) {
            Thread.currentThread().setContextClassLoader(IconUtils.class.getClassLoader());
            ImageIO.scanForPlugins();
            localContextUpdated.set(true);
            LOGGER.info("ImageIO plugins updated with TwelveMonkeys capabilities");
        }
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
                throw new IllegalArgumentException("IOException while trying to load image.");
            }
        }
        return null;
    }

    // backport from Image Viewer 2: remove unwanted SVG attributes
    private static ByteArrayInputStream sanitizeSVGImageBytes(byte[] imageBytes) {
        String contents = new String(imageBytes, Charset.defaultCharset());
        Matcher matcher = cssVarRe.matcher(contents);
        String replaced = matcher.replaceAll("currentColor");
        return new ByteArrayInputStream(replaced.getBytes());
    }

    public static ImageWrapper fromBase64(String base64, IconType iconType, double additionalUIScale) {
        return loadImage(B64_DECODER.decode(base64), iconType, additionalUIScale);
    }

    private static ImageWrapper loadImage(byte[] imageBytes, IconType iconType, double additionalUIScale) {
        enhanceImageIOCapabilities();

        if (iconType == IconType.SVG) {
            // backport from Image Viewer 2: no longer rely on ImageIO plugins for SVG rendering
            try {
                TranscodingHints transcoderHints = localTranscoderHints.get();
                if (transcoderHints == null) {
                    transcoderHints = new TranscodingHints();
                    transcoderHints.put(ImageTranscoder.KEY_HEIGHT, SVG_SIZE_BEFORE_RESCALING);
                    transcoderHints.put(ImageTranscoder.KEY_WIDTH, SVG_SIZE_BEFORE_RESCALING);
                    transcoderHints.put(ImageTranscoder.KEY_XML_PARSER_VALIDATING, false);
                    transcoderHints.put(ImageTranscoder.KEY_DOCUMENT_ELEMENT_NAMESPACE_URI, SVGConstants.SVG_NAMESPACE_URI);
                    transcoderHints.put(ImageTranscoder.KEY_DOCUMENT_ELEMENT, "svg"); //NON-NLS

                    DOMImplementation domImplementation = localSVGDOMImplementation.get();
                    if (domImplementation == null) {
                        domImplementation = SVGDOMImplementation.getDOMImplementation();
                        localSVGDOMImplementation.set(domImplementation);
                    }
                    transcoderHints.put(ImageTranscoder.KEY_DOM_IMPLEMENTATION, domImplementation);

                    localTranscoderHints.set(transcoderHints);
                }
                ByteArrayInputStream inputStream = sanitizeSVGImageBytes(imageBytes);
                TranscoderInput transcoderInput = new TranscoderInput(inputStream);
                BufferedImage[] imagePointer = new BufferedImage[1];
                ImageTranscoder t = new ImageTranscoder() {

                    @Override
                    public BufferedImage createImage(int w, int h) {
                        return ImageUtil.createImage(w, h, BufferedImage.TYPE_INT_ARGB);
                    }

                    @Override
                    public void writeImage(BufferedImage bufferedImage, TranscoderOutput transcoderOutput) {
                        imagePointer[0] = bufferedImage;
                    }
                };
                t.setTranscodingHints(transcoderHints);
                t.transcode(transcoderInput, null);
                BufferedImage bufferedImage = imagePointer[0];
                Image thumbnail = scaleImage(bufferedImage);
                if (thumbnail != null) {
                    JBImageIcon scaledJBImageWhichNeedsRescale;
                    Image scaledImage;
                    if (additionalUIScale == SettingsService.DEFAULT_ADDITIONAL_UI_SCALE) { // TODO test 1.25 scale on linux
                        scaledImage = IconUtil.createImageIcon(thumbnail).getImage();
                    } else {
                        scaledJBImageWhichNeedsRescale = IconUtil.createImageIcon(thumbnail);
                        scaledImage = ImageLoader.scaleImage(scaledJBImageWhichNeedsRescale.getImage(), additionalUIScale);
                    }
                    return new ImageWrapper(iconType, scaledImage, imageBytes);
                }
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
        Image scaledImage = scaleImage(image);
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

    private static Image scaleImage(Image image) {
        return ImageUtil.scaleImage(image, SCALING_SIZE, SCALING_SIZE);
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
