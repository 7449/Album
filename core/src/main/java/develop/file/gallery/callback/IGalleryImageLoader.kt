package develop.file.gallery.callback

import android.widget.FrameLayout
import develop.file.gallery.entity.ScanEntity

interface IGalleryImageLoader {
    fun onDisplayHomeGallery(width: Int, height: Int, entity: ScanEntity, container: FrameLayout) {}
    fun onDisplayFinderGallery(entity: ScanEntity, container: FrameLayout) {}
    fun onDisplayPrevGallery(entity: ScanEntity, container: FrameLayout) {}
}