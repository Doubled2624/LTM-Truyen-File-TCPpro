package app;

public final class Protocol {
    public static final String MSG_REQ   = "REQ";   // REQ|filename|size
    public static final String MSG_ACPT  = "ACPT";
    public static final String MSG_REJ   = "REJ";
    public static final String MSG_DONE  = "DONE";
    public static final String SEP       = "|";

    public static boolean isImage(String name) {
        if (name == null) return false;
        String n = name.toLowerCase();
        return n.endsWith(".jpg") || n.endsWith(".jpeg")
                || n.endsWith(".png") || n.endsWith(".gif");
    }
}
