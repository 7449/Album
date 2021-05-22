package com.gallery.scan.result

sealed class Result<R> {

    data class Multiple<T>(val data: ArrayList<T>) : Result<T>()

    data class Single<T>(val data: T?) : Result<T>()

    val multipleValue: ArrayList<R>
        get() {
            if (this is Multiple) {
                return data
            }
            throw KotlinNullPointerException("$this")
        }

    val singleValue: R?
        get() {
            if (this is Single) {
                return data
            }
            throw KotlinNullPointerException("$this")
        }

    override fun toString(): String {
        return when (this) {
            is Multiple<*> -> "Multiple[data=$data]"
            is Single -> "Single[data=$data]"
        }
    }

}