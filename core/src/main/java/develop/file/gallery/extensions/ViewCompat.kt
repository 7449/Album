package develop.file.gallery.extensions

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator

internal object ViewCompat {

    internal fun View.hide(): View = apply { if (!isGone) visibility = View.GONE }

    internal fun View.show(): View = apply { if (!isVisible) visibility = View.VISIBLE }

    internal fun RecyclerView.ViewHolder.setOnClick(action: (position: Int) -> Unit) = apply {
        itemView.setOnClickListener {
            val position = bindingAdapterPosition
            if (position == RecyclerView.NO_POSITION) return@setOnClickListener
            action.invoke(position)
        }
    }

    internal fun RecyclerView.supportsChangeAnimations(boolean: Boolean) {
        if (itemAnimator is SimpleItemAnimator) {
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = boolean
        }
    }

    internal fun ViewGroup.verticalLinear(display: Int): LinearLayout {
        return LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(display, display)
        }
    }

    internal fun LinearLayout.imageView(divider: Int): AppCompatImageView {
        return AppCompatImageView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            ).apply {
                setMargins(divider, divider, divider, 0)
                weight = 1f
            }
        }
    }

    internal fun LinearLayout.textView(divider: Int): AppCompatTextView {
        return AppCompatTextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(divider, 0, divider, divider)
                setPadding(0, 0, 0, 10)
            }
            gravity = Gravity.CENTER
        }
    }

    internal fun ViewGroup.frame(divider: Int, display: Int): FrameLayout {
        return FrameLayout(context).apply {
            layoutParams = FrameLayout.LayoutParams(display, display)
                .apply { setPadding(divider) }
        }
    }

    internal fun FrameLayout.checkBox(divider: Int, display: Int): AppCompatTextView {
        return AppCompatTextView(context).apply {
            layoutParams = FrameLayout.LayoutParams(display / 6, display / 6).apply {
                setPadding(divider)
                setMargins(divider)
                gravity = Gravity.END
            }
            hide()
        }
    }

}