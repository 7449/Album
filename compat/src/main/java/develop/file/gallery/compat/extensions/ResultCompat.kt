package develop.file.gallery.compat.extensions

import android.os.Bundle
import android.os.Parcelable

object ResultCompat {

    inline fun <reified T : Parcelable> Bundle?.parcelableArrayList(key: String): ArrayList<T> =
        getObj(key) { arrayListOf() }

    inline fun <reified T> Bundle?.getObj(key: String, action: () -> T): T = this?.get(key) as? T
        ?: action.invoke()

}