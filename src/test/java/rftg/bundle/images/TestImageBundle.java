package rftg.bundle.images;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.EnumMap;

/**
 * Unit test for simple App.
 */
public class TestImageBundle extends TestCase {
    private static EnumMap<ImageType, Integer> fixture;

    static {
        fixture = new EnumMap<ImageType, Integer>(ImageType.class);
        fixture.put(ImageType.CARDBACK, 1);
        fixture.put(ImageType.ACTIONS, 9);
        fixture.put(ImageType.CARDS, 191);
        fixture.put(ImageType.GOALS, 20);
        fixture.put(ImageType.ICONS, 17);

    }

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public TestImageBundle(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(TestImageBundle.class);
    }

    public void testDisassemble() throws IOException {

        ImageBundle imageBundle = new ImageBundle("images.data");
        File file = new File("images");
        if (!file.exists()) {
            file.mkdir();
        }

        String outputPath = imageBundle.disassemble("images");

        assertEquals(file.getAbsolutePath(), outputPath);
        for (ImageType imageType : ImageType.values()) {
            File sub = new File(file, imageType.getName());
            assertTrue(sub.exists());
            assertTrue(sub.isDirectory());
            assertEquals(imageType.getName(), fixture.get(imageType).intValue(), sub.list().length);
        }
    }

    public void testDisassembleOutputDirDontExist() throws IOException {

        ImageBundle imageBundle = new ImageBundle("images.data");
        File file = new File("images2");
        assertFalse(file.exists());
        assertTrue(file.mkdir());

        String outputPath = imageBundle.disassemble("images2");

        assertEquals(file.getAbsolutePath(), outputPath);
        for (ImageType imageType : ImageType.values()) {
            File sub = new File(file, imageType.getName());
            assertTrue(sub.exists());
            assertTrue(sub.isDirectory());
            assertEquals(imageType.getName(), fixture.get(imageType).intValue(), sub.list().length);
        }
    }

    public void testLoad() {
        ImageBundle imageBundle = new ImageBundle("images.data");
        imageBundle.load();
        for (ImageType imageType : ImageType.values()) {
            assertEquals(imageType.getName(), fixture.get(imageType).intValue(), imageBundle.count(imageType));
        }

    }

    @Override
    protected void tearDown() throws Exception {
        File file = new File("images2");
        if (file.exists() && file.isDirectory()) {
            FileUtils.deleteDirectory(file);
        }
    }
}
