package rftg.bundle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Bundle {
    Map<ImageType, ImageCache> cacheMap = new HashMap<ImageType, ImageCache>();
    private String bundleName;

    public Bundle(String bundleName) {
        this.bundleName = bundleName;
        initializeCache();
    }

    private void initializeCache() {
        ImageType[] values = ImageType.values();
        for (ImageType value : values) {
            cacheMap.put(value, new ImageCache());
        }
    }

    public void load() {
        Loader loader = new Loader();
        loader.load(bundleName, new Loader.LoadCallback() {
            public void execute(ImageType type, int index, byte[] imageData) {
                try {
                    BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageData));
                    cacheMap.get(type).put(index, bufferedImage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


    public String disassemble(final String outputDir) throws IOException {
        ensureDirectory(outputDir + "/actions");
        ensureDirectory(outputDir + "/cards");
        ensureDirectory(outputDir + "/cardback");
        ensureDirectory(outputDir + "/goals");
        ensureDirectory(outputDir + "/icons");


        Loader loader = new Loader();
        loader.load(bundleName, new Loader.LoadCallback() {
            public void execute(ImageType dir, int index, byte[] imageData) {
                String filename = outputDir + "/" + dir.getName() + "/" + dir.getName() + "-" + index + ".jpg";
                try {
                    FileOutputStream out = new FileOutputStream(filename);
                    out.write(imageData);
                    out.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return new File(outputDir).getAbsolutePath();
    }

    private void ensureDirectory(String pathname) {
        File file = new File(pathname);
        if (!file.exists()) file.mkdirs();
    }

    public BufferedImage getCard(int index) {
        return getFromCache(ImageType.CARDS, index);
    }

    public BufferedImage getAction(int index) {
        return getFromCache(ImageType.ACTIONS, index);
    }

    public BufferedImage getCardBack() {
        return getFromCache(ImageType.CARDBACK, 0);
    }

    public BufferedImage getGoal(int index) {
        return getFromCache(ImageType.GOALS, index);
    }

    public BufferedImage getIcon(int index) {
        return getFromCache(ImageType.ICONS, index);
    }

    private BufferedImage getFromCache(ImageType type, int index) {
        return cacheMap.get(type).get(index);
    }

    public int count(ImageType type) {
        return cacheMap.get(type).size();
    }
}
