package develop.file.gallery.ui.wechat.extension

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

internal object TimeCompat {

    @SuppressLint("SimpleDateFormat")
    private val formatter = SimpleDateFormat("yyyy/MM")

    internal fun Long.formatTimeVideo(): String {
        if (toInt() == 0) {
            return "--:--"
        }
        val format: String = String.format(
            "%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(this),
            TimeUnit.MILLISECONDS.toSeconds(this) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(this))
        )
        if (!format.startsWith("0")) {
            return format
        }
        return format.substring(1)
    }

    internal fun Long.formatTime(): String {
        if (toInt() == 0) {
            return "--/--"
        }
        return formatter.format(this * 1000)
    }

}