package com.gallery.scan.args

sealed class MediaResult<R> {

    data class Multiple<T>(val data: ArrayList<T>) : MediaResult<T>()

    data class Single<T>(val data: T?) : MediaResult<T>()

    val multipleValue: ArrayList<R>
        get() = if (this is Multiple) data else throw KotlinNullPointerException("$this")

    val singleValue: R?
        get() = if (this is Single) data else throw KotlinNullPointerException("$this")

    override fun toString(): String {
        return when (this) {
            is Multiple<*> -> "Multiple[data=$data]"
            is Single -> "Single[data=$data]"
        }
    }

}