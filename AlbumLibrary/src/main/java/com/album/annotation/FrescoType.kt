package com.album.annotation

import android.support.annotation.IntDef
import com.album.AlbumConstant

/**
 * by y on 22/08/2017.
 */

@IntDef(AlbumConstant.TYPE_FRESCO_ALBUM, AlbumConstant.TYPE_FRESCO_PREVIEW)
@Retention(AnnotationRetention.SOURCE)
annotation class FrescoType
