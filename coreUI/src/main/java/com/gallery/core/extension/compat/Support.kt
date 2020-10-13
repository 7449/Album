package com.gallery.core.extension.compat

import androidx.appcompat.app.AppCompatActivity
import com.gallery.core.extension.fragment.PrevFragment
import com.gallery.core.extension.fragment.ScanFragment

/** [ScanFragment] */
val AppCompatActivity.galleryFragment: ScanFragment get() = supportFragmentManager.findFragmentByTag(ScanFragment::class.java.simpleName) as ScanFragment

/** [PrevFragment] */
val AppCompatActivity.prevFragment: PrevFragment get() = supportFragmentManager.findFragmentByTag(PrevFragment::class.java.simpleName) as PrevFragment