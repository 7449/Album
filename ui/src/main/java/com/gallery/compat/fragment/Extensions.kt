package com.gallery.compat.fragment

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

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

fun AppCompatActivity.addFragmentExpand(
    id: Int,
    fragmentType: FragmentType = FragmentType.COMMIT_ALLOWING_STATE_LOSS,
    fragment: Fragment,
) = supportFragmentManager.beginTransaction().add(id, fragment, fragment.javaClass.simpleName)
    .commitExpand(fragmentType)

fun AppCompatActivity.showFragmentExpand(
    fragmentType: FragmentType = FragmentType.COMMIT_ALLOWING_STATE_LOSS,
    fragment: Fragment,
) = supportFragmentManager.beginTransaction().show(fragment).commitExpand(fragmentType)

internal fun FragmentTransaction.commitExpand(fragmentType: FragmentType) {
    when (fragmentType) {
        FragmentType.COMMIT -> commit()
        FragmentType.COMMIT_ALLOWING_STATE_LOSS -> commitAllowingStateLoss()
        FragmentType.NOW -> commitNow()
        FragmentType.NOW_ALLOWING_STATE_LOSS -> commitNowAllowingStateLoss()
    }
}

enum class FragmentType {
    COMMIT,
    COMMIT_ALLOWING_STATE_LOSS,
    NOW,
    NOW_ALLOWING_STATE_LOSS
}
