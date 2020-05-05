package com.gallery.ui.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.View

class AnimUtils(private val height: Int) {

    companion object {
        fun newInstance(height: Int): AnimUtils {
            return AnimUtils(height)
        }
    }

    fun openAnim(v: View, action: () -> Unit) {
        v.visibility = View.VISIBLE
        val animator = createDropAnimator(v, 0, height)
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                action.invoke()
            }
        })
        animator.start()
    }

    fun closeAnimate(view: View, action: () -> Unit) {
        val origWidth = view.width
        val animator = createDropAnimator(view, origWidth, 0)
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                view.visibility = View.GONE
                action.invoke()
            }
        })
        animator.start()
    }

    private fun createDropAnimator(v: View, start: Int, end: Int): ValueAnimator {
        val animator = ValueAnimator.ofInt(start, end)
        animator.duration = 300
        animator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            val layoutParams = v.layoutParams
            layoutParams.height = value
            v.layoutParams = layoutParams
        }
        return animator
    }

}