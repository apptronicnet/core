package net.apptronic.core.viewmodel

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.context.component.IComponent
import net.apptronic.core.context.lifecycle.LifecycleStageDefinition
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.commons.setTo
import net.apptronic.core.entity.commons.value

/**
 * @see [propertyWithSetWhenStage]
 */
@UnderDevelopment
fun <T> IViewModel.propertyWithSetWhenAttached(
    clearOnExit: Boolean = false, sourceProvider: () -> Entity<T>
): Entity<T> = propertyWithSetWhenStage(
    ViewModelLifecycle.Attached, clearOnExit, sourceProvider
)

/**
 * @see [propertyWithSetWhenStage]
 */
@UnderDevelopment
fun <T> IViewModel.propertyWithSetWhenAttached(
    defaultValue: T, resetOnExit: Boolean = false, sourceProvider: () -> Entity<T>
): Entity<T> = propertyWithSetWhenStage(
    ViewModelLifecycle.Attached, defaultValue, resetOnExit, sourceProvider
)

/**
 * @see [propertyWithSetWhenStage]
 */
@UnderDevelopment
fun <T> IViewModel.propertyWithSetWhenBound(
    clearOnExit: Boolean = false, sourceProvider: () -> Entity<T>
): Entity<T> = propertyWithSetWhenStage(
    ViewModelLifecycle.Bound, clearOnExit, sourceProvider
)

/**
 * @see [propertyWithSetWhenStage]
 */
@UnderDevelopment
fun <T> IViewModel.propertyWithSetWhenBound(
    defaultValue: T, resetOnExit: Boolean = false, sourceProvider: () -> Entity<T>
): Entity<T> = propertyWithSetWhenStage(
    ViewModelLifecycle.Bound, defaultValue, resetOnExit, sourceProvider
)

/**
 * @see [propertyWithSetWhenStage]
 */
@UnderDevelopment
fun <T> IViewModel.propertyWithSetWhenVisible(
    clearOnExit: Boolean = false, sourceProvider: () -> Entity<T>
): Entity<T> = propertyWithSetWhenStage(
    ViewModelLifecycle.Visible, clearOnExit, sourceProvider
)

/**
 * @see [propertyWithSetWhenStage]
 */
@UnderDevelopment
fun <T> IViewModel.propertyWithSetWhenVisible(
    defaultValue: T, resetOnExit: Boolean = false, sourceProvider: () -> Entity<T>
): Entity<T> = propertyWithSetWhenStage(
    ViewModelLifecycle.Visible, defaultValue, resetOnExit, sourceProvider
)

/**
 * @see [propertyWithSetWhenStage]
 */
@UnderDevelopment
fun <T> IViewModel.propertyWithSetWhenFocused(
    clearOnExit: Boolean = false, sourceProvider: () -> Entity<T>
): Entity<T> = propertyWithSetWhenStage(
    ViewModelLifecycle.Focused, clearOnExit, sourceProvider
)

/**
 * @see [propertyWithSetWhenStage]
 */
@UnderDevelopment
fun <T> IViewModel.propertyWithSetWhenFocused(
    defaultValue: T, resetOnExit: Boolean = false, sourceProvider: () -> Entity<T>
): Entity<T> = propertyWithSetWhenStage(
    ViewModelLifecycle.Focused, defaultValue, resetOnExit, sourceProvider
)

/**
 * Create [Entity] which load value from other [Entity], provided by [sourceProvider]
 * each time when [ViewModel] entered [lifecycleStage].
 *
 * Advantage it that [sourceProvider] works only when [ViewModel] in specified [lifecycleStage] (including inner stages)
 * and automatically releases when specified stage is exited.
 *
 * @param clearOnExit define is on exit from [lifecycleStage] result [Entity] should be cleared
 * @param sourceProvider where to get entity on enter [lifecycleStage]
 */
@UnderDevelopment
fun <T> IComponent.propertyWithSetWhenStage(
    lifecycleStage: LifecycleStageDefinition,
    clearOnExit: Boolean = false,
    sourceProvider: () -> Entity<T>
): Entity<T> {
    val property = value<T>()
    onEnterStage(lifecycleStage) {
        sourceProvider().setTo(property)
        if (clearOnExit) {
            onExit {
                property.clear()
            }
        }
    }
    return property
}

/**
 * Create [Entity] which load value from other [Entity], provided by [sourceProvider]
 * each time when [ViewModel] entered [lifecycleStage].
 *
 * Advantage it that [sourceProvider] works only when [ViewModel] in specified [lifecycleStage] (including inner stages)
 * and automatically releases when specified stage is exited.
 *
 * @param defaultValue define default value for result [Entity] until [sourceProvider] result provides something
 * @param resetOnExit is it needed to reset result [Entity] to [defaultValue] each time [lifecycleStage] is exited
 * @param sourceProvider where to get entity on enter [lifecycleStage]
 */
@UnderDevelopment
fun <T> IComponent.propertyWithSetWhenStage(
    lifecycleStage: LifecycleStageDefinition,
    defaultValue: T,
    resetOnExit: Boolean,
    sourceProvider: () -> Entity<T>
): Entity<T> {
    val property = value<T>(defaultValue)
    onEnterStage(lifecycleStage) {
        sourceProvider().setTo(property)
        if (resetOnExit) {
            onExit {
                property.set(defaultValue)
            }
        }
    }
    return property
}