package com.gallery.compat.extensions

import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import com.gallery.compat.fragment.GalleryCompatFragment
import com.gallery.compat.fragment.PrevCompatFragment

val AppCompatActivity.galleryFragment: GalleryCompatFragment
    get() = supportFragmentManager.findFragmentByTag(
        GalleryCompatFragment::class.java.simpleName
    ) as GalleryCompatFragment

val AppCompatActivity.prevFragment: PrevCompatFragment
    get() = supportFragmentManager.findFragmentByTag(
        PrevCompatFragment::class.java.simpleName
    ) as PrevCompatFragment

inline fun <reified T : Parcelable> Bundle?.getParcelableExpand(key: String): T =
    getParcelableOrDefault(key)

inline fun <reified T : Parcelable> Bundle?.getParcelableArrayListExpand(key: String): ArrayList<T> =
    getObjExpand(key) { arrayListOf() }

inline fun <reified T : Parcelable> Bundle?.getParcelableOrDefault(
    key: String,
    defaultValue: Parcelable
    = this?.getParcelable<T>(key)!!
): T = getObjExpand(key) { defaultValue as T }

inline fun <reified T> Bundle?.getObjExpand(key: String, action: () -> T): T = this?.get(key) as? T
    ?: action.invoke()