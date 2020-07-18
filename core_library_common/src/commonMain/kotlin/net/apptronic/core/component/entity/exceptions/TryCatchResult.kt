package net.apptronic.core.component.entity.exceptions

sealed class TryCatchResult<T> {

    data class Success<T>(val result: T) : TryCatchResult<T>()

    data class Failure<T>(val exception: Exception) : TryCatchResult<T>()

}