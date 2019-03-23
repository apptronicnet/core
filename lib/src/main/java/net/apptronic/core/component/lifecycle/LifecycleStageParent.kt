package net.apptronic.core.component.lifecycle

internal interface LifecycleStageParent {

    fun onChildEnter()

    fun cancelOnExitFromActiveStage(eventCallback: EventCallback)

}