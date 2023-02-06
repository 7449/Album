package develop.file.gallery.extensions

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.ChecksSdkIntAtLeast
import develop.file.gallery.entity.ScanEntity
import develop.file.media.impl.file.FileMediaEntity

object ResultCompat {

    @ChecksSdkIntAtLeast(api = 29)
    internal fun hasQ(): Boolean = Build.VERSION.SDK_INT >= 29

    fun Bundle?.orEmpty(): Bundle = this ?: Bundle.EMPTY

    inline fun <reified T : Parcelable> Bundle.parcelableVersion(key: String): T? = when {
        Build.VERSION.SDK_INT >= 33 -> getParcelable(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelable(key) as? T
    }

    inline fun <reified T : Parcelable> Bundle.parcelableVersionNotNull(key: String): T =
        requireNotNull(parcelableVersion(key))

    fun List<FileMediaEntity>.toScanEntity(): ArrayList<ScanEntity> =
        mapTo(ArrayList()) { ScanEntity(it) }

    fun List<ScanEntity>.toFileMediaEntity(): ArrayList<FileMediaEntity> =
        mapTo(ArrayList()) { it.delegate }

    fun FileMediaEntity.toScanEntity(): ScanEntity = ScanEntity(this)

}