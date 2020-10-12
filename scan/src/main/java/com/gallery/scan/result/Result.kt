package com.gallery.scan.result

import com.gallery.scan.types.ResultType

sealed class Result<R> {

    data class Multiple<T>(val data: ArrayList<T>) : Result<T>()

    data class Single<T>(val data: T?) : Result<T>()

    data class Error(val type: String) : Result<Nothing>()

    @Deprecated("@see : result is Result.Multiple", ReplaceWith("result is Result.Multiple", "com.gallery.scan.result.Result.Multiple"))
    val isMultiple: Boolean
        get() = this is Multiple

    @Deprecated("@see : result is Result.Single", ReplaceWith("result is Result.Single", "com.gallery.scan.result.Result.Single"))
    val isSingle: Boolean
        get() = this is Single

    @Deprecated("@see : result is Result.Error", ReplaceWith("result is Result.Error", "com.gallery.scan.result.Result.Error"))
    val isError: Boolean
        get() = this is Error

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

    val isSingleError: Boolean
        get() = this is Error && type == ResultType.SINGLE

    val isMultipleError: Boolean
        get() = this is Error && type == ResultType.MULTIPLE

    override fun toString(): String {
        return when (this) {
            is Multiple<*> -> "Multiple[data=$data]"
            is Single -> "Single[data=$data]"
            is Error -> "Error[type=$type]"
        }
    }

}