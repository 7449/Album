package com.album.ui.dialog

import androidx.fragment.app.FragmentManager
import com.album.Album
import com.album.AlbumBundle

/**
 * @author y
 * @create 2019/3/1
 */

fun Album.dialog(fragmentManager: FragmentManager) = dialog(fragmentManager, AlbumDialogFragment::class.java.simpleName)

fun Album.dialog(fragmentManager: FragmentManager, tag: String) = dialog(AlbumBundle(), AlbumDialogUiBundle(), fragmentManager, tag)

fun Album.dialog(albumBundle: AlbumBundle, fragmentManager: FragmentManager) = dialog(albumBundle, AlbumDialogUiBundle(), fragmentManager, AlbumDialogFragment::class.java.simpleName)

fun Album.dialog(albumBundle: AlbumBundle, fragmentManager: FragmentManager, tag: String) = dialog(albumBundle, AlbumDialogUiBundle(), fragmentManager, tag)

fun Album.dialog(albumBundle: AlbumBundle, uiBundle: AlbumDialogUiBundle, fragmentManager: FragmentManager, tag: String) = AlbumDialogFragment.newInstance(albumBundle, uiBundle).show(fragmentManager, tag)

