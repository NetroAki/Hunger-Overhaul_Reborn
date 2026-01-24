package org.Netroaki.Main.util;
import org.Netroaki.Main.HOReborn;
public class VersionDetector {
    public static void init() {
        HOReborn.LOGGER.info("VersionDetector initialized (1.21.1 detected)");
    }
    
    public static boolean is1_20_1() {
        return false;
    }
    
    public static boolean is1_21_1() {
        return true;
    }
    
    public static String getVersionString() {
        return "1.21.1";
    }
}
