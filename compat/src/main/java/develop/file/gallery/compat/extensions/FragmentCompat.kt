package develop.file.gallery.compat.extensions

import androidx.fragment.app.Fragment

object FragmentCompat {

    inline fun <reified T> Fragment.galleryCallbackOrNull(): T? {
        return when {
            parentFragment is T -> parentFragment as T
            activity is T -> activity as T
            else -> null
        }
    }

    inline fun <reified T> Fragment.galleryCallback(): T {
        return galleryCallbackOrNull<T>()
            ?: throw IllegalArgumentException(context.toString() + " must implement ${T::class.java.simpleName}")
    }

    inline fun <reified T> Fragment.galleryCallbackOrNewInstance(action: () -> T): T {
        return galleryCallbackOrNull<T>() ?: action.invoke()
    }

}