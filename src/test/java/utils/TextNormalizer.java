package utils;

public class TextNormalizer {

    private TextNormalizer() {}

    public static String normalize(String s) {
        if (s == null) return "";

        String t = s.toLowerCase();
        t = t.replace("\u2019", "'").replace("’", "'");
        t = t.replace("weтve", "we've");
        t = t.replace("'", "");
        t = t.replace('\u00A0', ' ');
        t = t.replaceAll("\\s+", " ").trim();
        t = t.replaceAll("[^\\p{L}\\p{N}\\s]+", "");
        t = t.replaceAll("\\s+", " ").trim();

        return t;
    }
}