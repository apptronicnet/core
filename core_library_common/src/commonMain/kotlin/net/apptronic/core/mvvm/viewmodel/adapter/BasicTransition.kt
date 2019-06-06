package net.apptronic.core.mvvm.viewmodel.adapter

sealed class BasicTransition {

    object Fade : BasicTransition()

    object Forward : BasicTransition()

    object Back : BasicTransition()

}