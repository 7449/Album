package com.album.util;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * by y on 15/08/2017.
 */

public class DrawableUtil {

    public static Drawable getDrawable(Context context, int id) {
        Drawable drawable = context.getResources().getDrawable(id);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }

}
