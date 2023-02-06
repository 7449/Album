package develop.file.gallery.compat.extensions

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

object ContextCompat {

    fun Context.minimumDrawable(@DrawableRes id: Int): Drawable? =
        drawable(id)?.apply { setBounds(0, 0, minimumWidth, minimumHeight) }

    fun Context.drawable(@DrawableRes id: Int): Drawable? =
        if (id == 0) null else ContextCompat.getDrawable(this, id)

    fun Context.color(@ColorRes id: Int): Int =
        ContextCompat.getColor(this, id)

    fun Context.toast(msg: String) {
        if (msg.isNotEmpty()) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }

    fun Context.openVideo(uri: Uri, error: () -> Unit) {
        runCatching {
            val video = Intent(Intent.ACTION_VIEW)
            video.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            video.setDataAndType(uri, "video/*")
            startActivity(video)
        }.onFailure { error.invoke() }
    }

}