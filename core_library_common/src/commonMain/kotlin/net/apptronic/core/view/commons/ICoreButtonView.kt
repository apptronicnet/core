package net.apptronic.core.view.commons

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.entity.base.UpdateEntity
import net.apptronic.core.view.ICoreContentView
import net.apptronic.core.view.ViewProperty

@UnderDevelopment
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

    fun onLongClick(action: () -> Unit) {
        onLongClick.set(action)
    }

    fun onLongClick(target: UpdateEntity<Unit>) {
        onLongClick.set {
            target.update(Unit)
        }
    }

}