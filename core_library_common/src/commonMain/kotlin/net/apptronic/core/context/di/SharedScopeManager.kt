package net.apptronic.core.context.di

interface SharedScopeManager {

    fun onSharedScopeInitialized(owner: SharedScopeOwner)

}