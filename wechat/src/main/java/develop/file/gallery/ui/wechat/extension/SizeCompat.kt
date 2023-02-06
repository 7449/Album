package develop.file.gallery.ui.wechat.extension

import java.text.DecimalFormat

internal object SizeCompat {

    internal fun Long.toFileSize(): String {
        return when {
            this < 1024 -> DecimalFormat().format(this) + " B"
            this < 1048576 -> DecimalFormat().format(this / 1024) + " KB"
            this < 1073741824 -> DecimalFormat().format(this / 1048576) + " MB"
            else -> DecimalFormat().format(this / 1073741824) + " GB"
        }
    }

}