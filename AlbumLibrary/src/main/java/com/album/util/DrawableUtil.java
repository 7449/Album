package com.album.util;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

/**
 * by y on 15/08/2017.
 */

public class DrawableUtil {

    public static Drawable getDrawable(Context context, int id, int color) {
        Drawable drawable = context.getResources().getDrawable(id);
        drawable.setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_ATOP);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }

}
