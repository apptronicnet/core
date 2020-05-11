package net.apptronic.core.android.viewmodel.listadapters

enum class BindingStrategy {
    /**
     * Immediately unbind ViewModel when recyclerview called onViewRecycled
     */
    MatchRecycle,

    /**
     * Unbind only when view is reused to bind to new ViewModel
     */
    UntilReused
}