package net.apptronic.common.core.component.lifecycle

internal interface LifecycleStageParent {

    fun onChildEnter()

    fun cancelOnExitFromActiveStage(eventCallback: EventCallback)

}