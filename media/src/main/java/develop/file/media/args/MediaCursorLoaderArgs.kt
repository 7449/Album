package develop.file.media.args

import android.net.Uri
import android.os.Bundle

open class MediaCursorLoaderArgs(
    open val uri: Uri,
    open val projection: Array<String> = arrayOf(),
    open val sortOrder: String = "",
) {

    companion object {
        internal const val DEFAULT_NULL_ID = 123456789.toLong()
    }

    open fun createSelection(args: Bundle): String = ""

}