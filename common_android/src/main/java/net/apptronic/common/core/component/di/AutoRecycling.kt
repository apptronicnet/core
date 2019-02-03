package net.apptronic.common.core.component.di

/**
 * Interface declaring that class requires recycling after usage in DI.
 * DI system will detect when item is really created and will destroy call [onAutoRecycle]
 * when lifecycle in which it is called will be destroyed
 */
interface AutoRecycling {

    fun onAutoRecycle()

}