package com.gallery.ui.compat

import androidx.appcompat.app.AppCompatActivity
import com.gallery.ui.page.fragment.PrevFragment
import com.gallery.ui.page.fragment.ScanFragment

/** [ScanFragment] */
val AppCompatActivity.galleryFragment: ScanFragment get() = supportFragmentManager.findFragmentByTag(ScanFragment::class.java.simpleName) as ScanFragment

/** [PrevFragment] */
val AppCompatActivity.prevFragment: PrevFragment get() = supportFragmentManager.findFragmentByTag(PrevFragment::class.java.simpleName) as PrevFragment