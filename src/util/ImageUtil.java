package util;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
public class ImageUtil {
    public static Image readImageFromBlob(InputStream in) {
        try {
            if (in == null) return null;
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[4096];
            int n;
            while ((n = in.read(data)) != -1)
                buffer.write(data, 0, n);
            byte[] bytes = buffer.toByteArray();
            if (bytes.length == 0) return null;
            BufferedImage img = null;
            try {
                img = ImageIO.read(new ByteArrayInputStream(bytes));
            } catch (Exception ex) {
                System.err.println("⚠ ImageIO failed: " + ex.getMessage());
            }
            if (img != null) return img;
            Image fallback = Toolkit.getDefaultToolkit().createImage(bytes);
            MediaTracker tracker = new MediaTracker(new Canvas());
            tracker.addImage(fallback, 0);
            tracker.waitForAll();
            if (tracker.isErrorAny()) {
                System.err.println("⚠ Toolkit image failed to load.");
                return null;
            }
            return fallback;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Image resize(Image img, int w, int h) {
        if (img == null) return null;
        return img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
    }
}