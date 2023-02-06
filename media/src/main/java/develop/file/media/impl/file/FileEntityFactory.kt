package develop.file.media.impl.file

import android.provider.MediaStore
import develop.file.media.args.MediaEntityFactory
import develop.file.media.extensions.intOrDefault
import develop.file.media.extensions.longOrDefault
import develop.file.media.extensions.stringOrDefault

internal fun MediaEntityFactory.Companion.file(): MediaEntityFactory {
    return action {
        FileMediaEntity(
            it.longOrDefault(MediaStore.Files.FileColumns._ID),

            it.longOrDefault(MediaStore.Files.FileColumns.SIZE),
            it.stringOrDefault(MediaStore.Files.FileColumns.DISPLAY_NAME),
            it.stringOrDefault(MediaStore.Files.FileColumns.TITLE),
            it.longOrDefault(MediaStore.Files.FileColumns.DATE_ADDED),
            it.longOrDefault(MediaStore.Files.FileColumns.DATE_MODIFIED),
            it.stringOrDefault(MediaStore.Files.FileColumns.MIME_TYPE),
            it.intOrDefault(MediaStore.Files.FileColumns.WIDTH),
            it.intOrDefault(MediaStore.Files.FileColumns.HEIGHT),

            it.longOrDefault(MediaStore.Files.FileColumns.PARENT),
            it.stringOrDefault(MediaStore.Files.FileColumns.MEDIA_TYPE),

            it.intOrDefault(MediaStore.Images.ImageColumns.ORIENTATION),
            it.stringOrDefault(MediaStore.Images.ImageColumns.BUCKET_ID),
            it.stringOrDefault(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME),

            it.longOrDefault(MediaStore.Video.VideoColumns.DURATION)
        )
    }
}