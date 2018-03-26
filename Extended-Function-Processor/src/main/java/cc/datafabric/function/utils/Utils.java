package cc.datafabric.function.utils;

import java.io.InputStream;
import java.util.Scanner;

public final class Utils {

    private Utils(){}

    public static String resourceToString(String path){
        InputStream stream = Utils.class.getResourceAsStream(path);
        Scanner s = new Scanner(stream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
