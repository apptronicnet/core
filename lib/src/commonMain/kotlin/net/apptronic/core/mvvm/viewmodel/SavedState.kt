package net.apptronic.core.mvvm.viewmodel

class SavedState {

    private var map = mutableMapOf<String, Any?>()

    fun <T> get(key: String): T? {
        return map[key] as? T
    }

    fun <T> put(key: String, value: T?) {
        map[key] = value
    }

}