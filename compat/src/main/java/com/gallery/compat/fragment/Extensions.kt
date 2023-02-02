package com.gallery.compat.fragment

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

internal inline fun <reified T> Fragment.galleryCallbackOrNull(): T? {
    return when {
        parentFragment is T -> parentFragment as T
        activity is T -> activity as T
        else -> null
    }
}

internal inline fun <reified T> Fragment.galleryCallback(): T {
    return galleryCallbackOrNull<T>()
        ?: throw IllegalArgumentException(context.toString() + " must implement ${T::class.java.simpleName}")
}

internal inline fun <reified T> Fragment.galleryCallbackOrNewInstance(action: () -> T): T {
    return galleryCallbackOrNull<T>() ?: action.invoke()
}

@SuppressLint("CommitTransaction")
internal fun AppCompatActivity.addFragment(
    id: Int,
    fragmentType: FragmentType = FragmentType.COMMIT_ALLOWING_STATE_LOSS,
    fragment: Fragment,
) = supportFragmentManager.beginTransaction().add(id, fragment, fragment.javaClass.simpleName)
    .commit(fragmentType)

@SuppressLint("CommitTransaction")
internal fun AppCompatActivity.showFragment(
    fragmentType: FragmentType = FragmentType.COMMIT_ALLOWING_STATE_LOSS,
    fragment: Fragment,
) = supportFragmentManager.beginTransaction().show(fragment).commit(fragmentType)

internal fun FragmentTransaction.commit(fragmentType: FragmentType) {
    when (fragmentType) {
        FragmentType.COMMIT -> commit()
        FragmentType.COMMIT_ALLOWING_STATE_LOSS -> commitAllowingStateLoss()
        FragmentType.NOW -> commitNow()
        FragmentType.NOW_ALLOWING_STATE_LOSS -> commitNowAllowingStateLoss()
    }
}

internal enum class FragmentType {
    COMMIT,
    COMMIT_ALLOWING_STATE_LOSS,
    NOW,
    NOW_ALLOWING_STATE_LOSS
}
