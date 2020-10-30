package com.gallery.compat.activity

import androidx.appcompat.app.AppCompatActivity
import com.gallery.compat.fragment.GalleryCompatFragment
import com.gallery.compat.fragment.PrevCompatFragment

/** [GalleryCompatFragment] */
val AppCompatActivity.galleryFragment: GalleryCompatFragment get() = supportFragmentManager.findFragmentByTag(GalleryCompatFragment::class.java.simpleName) as GalleryCompatFragment

/** [PrevCompatFragment] */
val AppCompatActivity.prevFragment: PrevCompatFragment get() = supportFragmentManager.findFragmentByTag(PrevCompatFragment::class.java.simpleName) as PrevCompatFragment
