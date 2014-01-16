package com.bulgogi.flag.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

public class Utils {
    static public boolean isKorea(Context context) {
        return context.getResources().getConfiguration().locale.toString().equals("ko_KR");
    }

    static public Bitmap makeTranparent(Bitmap transparent) {
        Bitmap nonTranparent = Bitmap.createBitmap(transparent.getWidth(), transparent.getHeight(), transparent.getConfig());  // Create another transparent the same size
        nonTranparent.eraseColor(Color.WHITE);  // set its background to white, or whatever color you want
        Canvas canvas = new Canvas(nonTranparent);  // create a canvas to draw on the new transparent
        canvas.drawBitmap(transparent, 0f, 0f, null); // draw old transparent on the background
        transparent.recycle();  // clear out old transparent
        return nonTranparent;
    }
}
