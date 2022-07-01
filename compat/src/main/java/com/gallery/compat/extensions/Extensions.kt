package com.gallery.compat.extensions

import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
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

inline fun <reified T : Parcelable> Bundle?.parcelableExpand(key: String): T =
        parcelableOrDefault(key)

inline fun <reified T : Parcelable> Bundle?.parcelableArrayListExpand(key: String): ArrayList<T> =
        getObjExpand(key) { arrayListOf() }

inline fun <reified T : Parcelable> Bundle?.parcelableOrDefault(
        key: String,
        defaultValue: Parcelable
        = this?.getParcelable<T>(key)!!
): T = getObjExpand(key) { defaultValue as T }

inline fun <reified T> Bundle?.getObjExpand(key: String, action: () -> T): T = this?.get(key) as? T
        ?: action.invoke()