package net.apptronic.core.mvvm.viewmodel.navigation

enum class BackNavigationStatus {

    /**
     * Back navigation is not possible
     */
    Disabled,

    /**
     * Back navigation is not allowed, but allowed to react on gesture. This means that gesture
     * detector will ignore confirmation event and will always play cancel transition
     */
    Restricted,

    /**
     * Allows back navigation with gestures
     */
    Allowed

}