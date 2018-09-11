package com.album.listener

import android.view.View
import org.jetbrains.annotations.NotNull

/**
 * by y on 22/08/2017.
 */

interface OnEmptyClickListener {
    fun click(@NotNull view: View): Boolean
}
