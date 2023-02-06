package develop.file.gallery.extensions

import android.content.Context
import android.net.Uri
import android.provider.MediaStore

object UriCompat {

    fun Uri.exists(context: Context): Boolean {
        return runCatching {
            context.contentResolver.openAssetFileDescriptor(this, "r")?.close()
        }.isSuccess
    }

    fun Uri.delete(context: Context) {
        runCatching {
            context.contentResolver.delete(this, null, null)
        }
    }

    fun Uri.id(context: Context): Long {
        val split = toString().split("/")
        var id = -1L
        runCatching {
            id = split[split.size - 1].toLong()
        }.onFailure {
            context.contentResolver.query(
                this,
                arrayOf(MediaStore.Files.FileColumns._ID),
                null,
                null,
                null
            ).use {
                val cursor = it ?: return@use
                while (cursor.moveToNext()) {
                    val columnIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)
                    if (columnIndex == -1) return@use
                    id = cursor.getLong(columnIndex)
                }
            }
        }
        return id
    }

    fun Uri.data(context: Context): String? {
        context.contentResolver.query(
            this,
            arrayOf(MediaStore.Files.FileColumns.DATA),
            null,
            null,
            null
        ).use {
            val cursor = it ?: return null
            while (cursor.moveToNext()) {
                val columnIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DATA)
                if (columnIndex == -1) return null
                return cursor.getString(columnIndex)
            }
            return null
        }
    }

}