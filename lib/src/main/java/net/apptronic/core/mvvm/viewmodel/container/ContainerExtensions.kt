package net.apptronic.core.mvvm.viewmodel.container

internal fun <T> getDiff(before: List<T>, after: List<T>): Diff<T> {
    val same = before.filter {
        after.contains(it)
    }
    val added = after.filter {
        same.contains(it).not()
    }
    val removed = before.filter {
        same.contains(it).not()
    }
    return Diff(added, removed)
}

internal data class Diff<T>(
    val added: List<T>,
    val removed: List<T>
)