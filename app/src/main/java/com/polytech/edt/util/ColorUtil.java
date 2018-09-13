package com.polytech.edt.util;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;

public class ColorUtil {

    public static Drawable coloredSVG(VectorDrawable drawable, int color) {
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        return drawable;
    }
}
