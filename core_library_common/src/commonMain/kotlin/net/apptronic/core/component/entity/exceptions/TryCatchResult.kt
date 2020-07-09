package net.apptronic.core.component.entity.exceptions

sealed class TryCatchResult<T> {

    class Success<T>(val result: T) : TryCatchResult<T>()

    class Failure<T>(val exception: Exception) : TryCatchResult<T>()

}