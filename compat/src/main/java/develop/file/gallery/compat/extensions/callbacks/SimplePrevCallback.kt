package develop.file.gallery.compat.extensions.callbacks

import android.os.Bundle
import develop.file.gallery.callback.IGalleryPrevCallback
import develop.file.gallery.delegate.IPrevDelegate
import develop.file.gallery.entity.ScanEntity

interface SimplePrevCallback : IGalleryPrevCallback {

    override fun onPageSelected(position: Int) {
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onSelectMultipleMaxCount() {
    }

    override fun onSelectMultipleFileChanged(position: Int, entity: ScanEntity) {
    }

    override fun onSelectMultipleFileNotExist(entity: ScanEntity) {
    }

    override fun onPrevCreated(delegate: IPrevDelegate, saveState: Bundle?) {
    }

}