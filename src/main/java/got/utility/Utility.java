package got.utility;

import java.awt.Container;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.WeakHashMap;

public class Utility {
    final static private ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    final static private Map cacheImages = new WeakHashMap(100);
    final static private MediaTracker mediaTracker = new MediaTracker(new Container());
    final static private Toolkit toolKit = Toolkit.getDefaultToolkit();

    final static public BufferedImage createImage(int i, int j, boolean flag) {
        if (flag) {
            return new BufferedImage(i, j, 2);
        } else {
            return new BufferedImage(i, j, BufferedImage.TYPE_INT_ARGB);
        }

    }

    public final static InputStream getResource(final String fileName) {
        return new BufferedInputStream(classLoader.getResourceAsStream(fileName));
    }

    final static public Image loadImage(final String fileName) {
        String keyName = fileName.trim().toLowerCase();
        Image cacheImage = (Image) cacheImages.get(keyName);
        if (cacheImage == null) {
            InputStream in = new BufferedInputStream(classLoader.getResourceAsStream(fileName));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                byte[] bytes = new byte[8192];
                int read;
                while ((read = in.read(bytes)) >= 0) {
                    byteArrayOutputStream.write(bytes, 0, read);
                }
                byte[] arrayByte = byteArrayOutputStream.toByteArray();
                cacheImages.put(keyName, cacheImage = toolKit.createImage(arrayByte));
                mediaTracker.addImage(cacheImage, 0);
                mediaTracker.waitForID(0);
                waitImage(100, cacheImage);
            } catch (Exception e) {
                throw new RuntimeException(fileName + " not found!");
            } finally {
                try {
                    if (byteArrayOutputStream != null) {
                        byteArrayOutputStream.close();
                        byteArrayOutputStream = null;
                    }
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                }
            }

        }
        if (cacheImage == null) {
            throw new RuntimeException(("File not found. ( " + fileName + " )").intern());
        }
        return cacheImage;
    }

    private final static void waitImage(int delay, Image image) {
        try {
            for (int i = 0; i < delay; i++) {
                if (toolKit.prepareImage(image, -1, -1, null)) {
                    return;
                }
                Thread.sleep(delay);
            }
        } catch (Exception e) {

        }
    }

    public static BufferedImage sacleImage(Image image, double ratioW, double ratioH) {
        BufferedImage sourceBI = new BufferedImage(image.getWidth(null), image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D gsource = (Graphics2D) sourceBI.getGraphics();
        gsource.drawImage(image, 0, 0, null);
        AffineTransform at = new AffineTransform();
        // scale image
        at.scale(ratioW, ratioH);
        BufferedImageOp bio;
        bio = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage destinationBI = bio.filter(sourceBI, null);
        return destinationBI;
    }

    public static Image rotateImage(Image inputImage,double degree) {
        BufferedImage sourceBI = new BufferedImage(inputImage.getWidth(null), inputImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = (Graphics2D) sourceBI.getGraphics();
        g.drawImage(inputImage, 0, 0, null);

        AffineTransform at = new AffineTransform();
        // rotate 45 degrees around image center
        at.rotate(degree * Math.PI / 180.0, sourceBI.getWidth() / 2.0, sourceBI.getHeight() / 2.0);

        /*
         * translate to make sure the rotation doesn't cut off any image data
         */
        AffineTransform translationTransform;
        translationTransform = findTranslation(at, sourceBI);
        at.preConcatenate(translationTransform);

        // instantiate and apply affine transformation filter
        BufferedImageOp bio;
        bio = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

        return  bio.filter(sourceBI, null);

    }
    private static AffineTransform findTranslation(AffineTransform at, BufferedImage bi) {
        Point2D p2din, p2dout;

        p2din = new Point2D.Double(0.0, 0.0);
        p2dout = at.transform(p2din, null);
        double ytrans = p2dout.getY();

        p2din = new Point2D.Double(0, bi.getHeight());
        p2dout = at.transform(p2din, null);
        double xtrans = p2dout.getX();

        AffineTransform tat = new AffineTransform();
        tat.translate(-xtrans, -ytrans);
        return tat;
      }
    public static int getStringPixelWidth(String str, Font font) {
        FontMetrics metrics = new FontMetrics(font) {
        };
        Rectangle2D bounds = metrics.getStringBounds(str, null);
        return (int) bounds.getWidth();
    }

    public static int getStringPixelHeight(String str, Font font) {
        FontMetrics metrics = new FontMetrics(font) {
        };
        Rectangle2D bounds = metrics.getStringBounds(str, null);
        return (int) bounds.getHeight();
    }
}