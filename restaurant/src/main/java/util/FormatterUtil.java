package util;


public class FormatterUtil {
    public static String trennlinie(int breite, char zeichen) {
        return String.valueOf(zeichen).repeat(Math.max(0, breite));
    }

    public static String zelleLinks(String text, int breite) {
        return String.format("%-" + breite + "s", text);
    }

    public static String zelleRechts(String text, int breite) {
        return String.format("%" + breite + "s", text);
    }

    // public static String preisFormat(double wert, int breite) {
    //     return zelleRechts(String.format("%.2f €", wert), breite);
    // }

    public static String preisMitEuro(double wert, int spaltenbreite) {
    return String.format("%" + (spaltenbreite-3) + ".2f €", wert);
    
   
}

}


