/*
 * Copyright 2018-2022 Polytech-EDT
 * Licensed under Apache License 2.0 or any later version
 * Refer to the LICENSE.md file included.
 */
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
