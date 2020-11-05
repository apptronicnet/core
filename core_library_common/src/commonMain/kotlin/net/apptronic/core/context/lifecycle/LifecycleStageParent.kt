package net.apptronic.core.context.lifecycle

internal interface LifecycleStageParent {

    fun onChildEnter()

    fun cancelOnExitFromActiveStage(eventCallback: EventCallback)

}