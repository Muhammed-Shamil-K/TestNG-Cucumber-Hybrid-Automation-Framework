package hybridAutomation.Core;

public enum Tag {

    SIGNE_NAME("Name of Signee", -96, -146),
    SIGN("Signature", -96, -120);

    public final String tagName;
    public final int xOffset;
    public final int yOffset;

    Tag(String tagName, int xOffset, int yOffset) {
        this.tagName = tagName;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }
}
