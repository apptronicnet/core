package net.apptronic.core.view.widgets.commons

import net.apptronic.core.component.entity.base.UpdateEntity
import net.apptronic.core.view.ICoreContentView
import net.apptronic.core.view.ViewProperty
import net.apptronic.core.view.binder.DynamicEntityReference
import net.apptronic.core.view.binder.DynamicReference

interface ICoreButtonView : ICoreContentView, IEnabledDisabledView {

    val onClick: ViewProperty<() -> Unit>

    val onLongClick: ViewProperty<() -> Unit>

    fun onClick(action: () -> Unit) {
        onClick.set(action)
    }

    fun onClick(target: UpdateEntity<Unit>) {
        onClick.set {
            target.update(Unit)
        }
    }

    fun onClick(action: DynamicReference<() -> Unit>) {
        onClick.set(action)
    }

    fun onClick(target: DynamicEntityReference<Unit, UpdateEntity<Unit>>) {
        target.subscribeWith(context) {
            onClick(it)
        }
    }

    fun onLongClick(action: () -> Unit) {
        onLongClick.set(action)
    }

    fun onLongClick(target: UpdateEntity<Unit>) {
        onLongClick.set {
            target.update(Unit)
        }
    }

    fun onLongClick(action: DynamicReference<() -> Unit>) {
        onLongClick.set(action)
    }

    fun onLongClick(target: DynamicEntityReference<Unit, UpdateEntity<Unit>>) {
        target.subscribeWith(context) {
            onLongClick(it)
        }
    }

}