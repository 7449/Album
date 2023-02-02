package com.gallery.compat.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.gallery.compat.fragment.GalleryCompatFragment
import com.gallery.compat.fragment.PrevCompatFragment

val AppCompatActivity.requireGalleryFragment: GalleryCompatFragment
    get() = requireNotNull(galleryFragment)

val AppCompatActivity.requirePrevFragment: PrevCompatFragment
    get() = requireNotNull(prevFragment)

val AppCompatActivity.galleryFragment: GalleryCompatFragment?
    get() = supportFragmentManager.findFragmentByTag(
        GalleryCompatFragment::class.java.simpleName
    ) as? GalleryCompatFragment

val AppCompatActivity.prevFragment: PrevCompatFragment?
    get() = supportFragmentManager.findFragmentByTag(
        PrevCompatFragment::class.java.simpleName
    ) as? PrevCompatFragment

internal fun Activity.intentResultOf(
    resultCode: Int,
    bundle: Bundle = bundleOf(),
    isFinish: Boolean = true
) {
    val intent = Intent()
    intent.putExtras(bundle)
    setResult(resultCode, intent)
    if (isFinish) {
        finish()
    }
}

inline fun <reified T : Parcelable> Bundle?.parcelableArrayList(key: String): ArrayList<T> =
    getObj(key) { arrayListOf() }

inline fun <reified T> Bundle?.getObj(key: String, action: () -> T): T = this?.get(key) as? T
    ?: action.invoke()

fun String?.toast(context: Context?) {
    if (!isNullOrEmpty() && context != null) {
        Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
    }
}