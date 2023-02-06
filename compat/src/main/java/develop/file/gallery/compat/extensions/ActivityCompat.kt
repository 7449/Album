package develop.file.gallery.compat.extensions

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import develop.file.gallery.compat.fragment.GalleryGridFragment
import develop.file.gallery.compat.fragment.GalleryPrevFragment

object ActivityCompat {

    val AppCompatActivity.requireGalleryFragment: GalleryGridFragment
        get() = requireNotNull(galleryFragment)

    val AppCompatActivity.requirePrevFragment: GalleryPrevFragment
        get() = requireNotNull(prevFragment)

    val AppCompatActivity.galleryFragment: GalleryGridFragment?
        get() = supportFragmentManager.findFragmentByTag(
            GalleryGridFragment::class.java.simpleName
        ) as? GalleryGridFragment

    val AppCompatActivity.prevFragment: GalleryPrevFragment?
        get() = supportFragmentManager.findFragmentByTag(
            GalleryPrevFragment::class.java.simpleName
        ) as? GalleryPrevFragment

    fun Activity.intentResultOf(
        resultCode: Int,
        bundle: Bundle = bundleOf(),
        isFinish: Boolean = true
    ) {
        val intent = Intent()
        intent.putExtras(bundle)
        setResult(resultCode, intent)
        if (isFinish) {
            finish()
        }
    }

    fun AppCompatActivity.addFragment(
        id: Int,
        fragmentType: FragmentType = FragmentType.COMMIT_ALLOWING_STATE_LOSS,
        fragment: Fragment,
    ) = supportFragmentManager.beginTransaction().add(id, fragment, fragment.javaClass.simpleName)
        .commit(fragmentType)

    fun AppCompatActivity.showFragment(
        fragmentType: FragmentType = FragmentType.COMMIT_ALLOWING_STATE_LOSS,
        fragment: Fragment,
    ) = supportFragmentManager.beginTransaction().show(fragment).commit(fragmentType)

    private fun FragmentTransaction.commit(fragmentType: FragmentType) {
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

}