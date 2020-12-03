package com.gallery.ui.wechat.extension

import android.view.animation.Animation

fun Animation.doOnAnimationStartExpand(action: (animation: Animation) -> Unit): Animation =
        setAnimationListenerExpand(onAnimationStart = action)

fun Animation.doOnAnimationEndExpand(action: (animation: Animation) -> Unit): Animation =
        setAnimationListenerExpand(onAnimationEnd = action)

fun Animation.doOnAnimationRepeatExpand(action: (animation: Animation) -> Unit): Animation =
        setAnimationListenerExpand(onAnimationRepeat = action)

fun Animation.setAnimationListenerExpand(
        onAnimationRepeat: (animation: Animation) -> Unit = {},
        onAnimationEnd: (animation: Animation) -> Unit = {},
        onAnimationStart: (animation: Animation) -> Unit = {},
): Animation {
    val listener = object : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation) {
            onAnimationRepeat.invoke(animation)
        }

        override fun onAnimationEnd(animation: Animation) {
            onAnimationEnd.invoke(animation)
        }

        override fun onAnimationStart(animation: Animation) {
            onAnimationStart.invoke(animation)
        }
    }
    setAnimationListener(listener)
    return this
}