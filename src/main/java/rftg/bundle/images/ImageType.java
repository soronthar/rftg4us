package rftg.bundle.images;

/**
 * Created by IntelliJ IDEA.
 * User: Rafael Alvarez
 * Date: 3/26/12
 * Time: 11:30 PM
 * To change this template use File | Settings | File Templates.
 */
public enum ImageType {
    CARDS(1, "cards", 4),
    CARDBACK(2, "cardback", 0),
    GOALS(3, "goals", 3),
    ICONS(4, "icons", 3),
    ACTIONS(5, "actions", 3);

    private int index;
    private String imageTypeName;
    private int bytesToRead;

    ImageType(int index, String name, int bytesToRead) {
        this.index = index;
        this.imageTypeName = name;
        this.bytesToRead = bytesToRead;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return imageTypeName;
    }

    public int getBytesToRead() {
        return bytesToRead;
    }
}
