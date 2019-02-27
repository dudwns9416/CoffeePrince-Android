package com.sc.coffeeprince.util;

import java.util.regex.Pattern;

/**
 * Created by SeungHyun on 2017-02-25.
 */

public class FomatUtils {
    public static boolean isEmail(String email) {
        if (email==null) return false;
        boolean b = Pattern.matches("[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+",email.trim());
        return b;
    }
}
