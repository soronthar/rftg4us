package rftg.bundle.images;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

class ImageLoader {

    public static final int HEADER_LENGHT = 4;
    public static final String MAGIC_NUMBER = "RFTG";

    public interface LoadCallback {
        void execute(ImageType dir, int index, byte[] imageData);
    }

    //TODO: test the error conditions... may need to build special files for it
    public void load(String bundleName, LoadCallback callback) {
        try {
            DataInputStream in = new DataInputStream(getClass().getClassLoader().getResourceAsStream(bundleName));

            consumeHeader(in);

            for (byte recordType = in.readByte(); recordType != 0; recordType = in.readByte()) {
                ImageType type = findImageTypeByIndex(recordType);
                int index = readInteger(in, type.getBytesToRead());
                byte[] imageData = readImage(in);

                callback.execute(type, index, imageData);

            }
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] readImage(InputStream in) throws IOException {
        int imageLength = readInteger(in, 8);
        byte[] imageData = new byte[imageLength];
        int count = in.read(imageData);
        if (count < imageLength) {
            throw new RuntimeException("Wrong data");
        }
        return imageData;
    }

    private ImageType findImageTypeByIndex(int recordType) {
        ImageType[] values = ImageType.values();
        for (ImageType value : values) {
            if (value.getIndex() == recordType) return value;
        }
        throw new RuntimeException("Wrong Record: " + recordType);
    }

    private void consumeHeader(InputStream in) throws IOException {
        byte[] buff = new byte[HEADER_LENGHT];

        int read = in.read(buff);
        if (read == -1) {
            throw new RuntimeException("wrong file.. too short");
        }

        String header = new String(buff);
        if (!header.equals(MAGIC_NUMBER)) {
            throw new RuntimeException("Image Bundle not for RFTG: Wrong Header");
        }
    }


    private static int readInteger(InputStream in, int bytesToRead) throws IOException {
        if (bytesToRead == 0) return -1;
        byte[] buf;
        int index;
        buf = new byte[bytesToRead];
        in.read(buf);
        index = Integer.parseInt(new String(buf).trim());
        return index;
    }

}
