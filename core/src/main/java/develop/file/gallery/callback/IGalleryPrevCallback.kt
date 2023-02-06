package develop.file.gallery.callback

import android.os.Bundle
import develop.file.gallery.delegate.IPrevDelegate
import develop.file.gallery.entity.ScanEntity

interface IGalleryPrevCallback {

    fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int)
    fun onPageSelected(position: Int)
    fun onPageScrollStateChanged(state: Int)

    fun onPrevCreated(delegate: IPrevDelegate, saveState: Bundle?)
    fun onSelectMultipleFileNotExist(entity: ScanEntity)
    fun onSelectMultipleMaxCount()
    fun onSelectMultipleFileChanged(position: Int, entity: ScanEntity)

}