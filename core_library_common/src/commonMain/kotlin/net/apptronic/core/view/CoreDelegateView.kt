package net.apptronic.core.view

class CoreDelegateView(private val delegate: ICoreDelegatedView) : ICoreView by delegate.view {

    val delegatedView: ICoreView = delegate.view

}