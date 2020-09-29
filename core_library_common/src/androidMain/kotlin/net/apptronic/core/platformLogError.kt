package net.apptronic.core

import android.util.Log

actual fun platformLogError(error: Error) {
    Log.e("apptronic.net/core", "Internal", error)
}