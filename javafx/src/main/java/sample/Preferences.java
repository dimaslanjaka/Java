package sample;

public class Preferences {
    @SuppressWarnings("unused")
    public static java.util.prefs.Preferences init() {
        return java.util.prefs.Preferences.userNodeForPackage(Preferences.class);
    }

    @SuppressWarnings("unused")
    public static java.util.prefs.Preferences init(Class cl) {
        return java.util.prefs.Preferences.userNodeForPackage(cl);
    }
}
