package com.bulgogi.flag.util;

import android.content.Context;

public class Utils {
    static public boolean isKorea(Context context) {
        return context.getResources().getConfiguration().locale.toString().equals("ko_KR");
    }
}
