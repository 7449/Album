package com.album.util

import android.content.Intent
import android.os.Bundle

object NullUtils {

    fun checkNotStringNull(notNull: String?): String {
        return notNull ?: ""
    }

    fun checkNotBundleNull(notNull: Bundle?): Bundle {
        return notNull ?: Bundle.EMPTY
    }

    fun checkNotIntentNull(notNull: Intent?): Intent {
        return notNull ?: Intent()
    }
}