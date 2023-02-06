package develop.file.gallery.ui.wechat.extension

import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation

internal object AnimCompat {

    internal val rotateAnimationSupport = RotateAnimation(
        0.toFloat(),
        180.toFloat(),
        Animation.RELATIVE_TO_SELF,
        0.5.toFloat(),
        Animation.RELATIVE_TO_SELF,
        0.5.toFloat()
    ).apply {
        interpolator = LinearInterpolator()
        duration = 200
        fillAfter = true
    }

    internal val rotateAnimationSupport2 = RotateAnimation(
        180.toFloat(),
        360.toFloat(),
        Animation.RELATIVE_TO_SELF,
        0.5.toFloat(),
        Animation.RELATIVE_TO_SELF,
        0.5.toFloat()
    ).apply {
        interpolator = LinearInterpolator()
        duration = 200
        fillAfter = true
    }

    internal fun Animation.doOnAnimationEnd(action: (animation: Animation) -> Unit): Animation =
        setAnimationListener(onAnimationEnd = action)

    private fun Animation.setAnimationListener(
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

}