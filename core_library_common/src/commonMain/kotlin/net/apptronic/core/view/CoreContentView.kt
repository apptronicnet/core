package net.apptronic.core.view

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.view.binder.DynamicEntityReference
import net.apptronic.core.view.binder.DynamicReference
import net.apptronic.core.view.properties.HorizontalAlignment
import net.apptronic.core.view.properties.VerticalAlignment

interface CoreContentView : ICoreView {

    val contentAlignmentVertical: ViewProperty<VerticalAlignment>
    val contentAlignmentHorizontal: ViewProperty<HorizontalAlignment>

    fun contentAlignmentVertical(vertical: VerticalAlignment) {
        contentAlignmentVertical.set(vertical)
    }

    fun contentAlignmentVertical(vertical: Entity<VerticalAlignment>) {
        contentAlignmentVertical.set(vertical)
    }

    fun contentAlignmentVertical(vertical: DynamicReference<VerticalAlignment>) {
        contentAlignmentVertical.set(vertical)
    }

    fun contentAlignmentVertical(vertical: DynamicEntityReference<VerticalAlignment, Entity<VerticalAlignment>>) {
        contentAlignmentVertical.set(vertical)
    }

    fun contentAlignmentHorizontal(horizontal: HorizontalAlignment) {
        contentAlignmentHorizontal.set(horizontal)
    }

    fun contentAlignmentHorizontal(horizontal: Entity<HorizontalAlignment>) {
        contentAlignmentHorizontal.set(horizontal)
    }

    fun contentAlignmentHorizontal(horizontal: DynamicReference<HorizontalAlignment>) {
        contentAlignmentHorizontal.set(horizontal)
    }

    fun contentAlignmentHorizontal(horizontal: DynamicEntityReference<HorizontalAlignment, Entity<HorizontalAlignment>>) {
        contentAlignmentHorizontal.set(horizontal)
    }

    fun contentAlignment(horizontal: HorizontalAlignment, vertical: VerticalAlignment) {
        contentAlignmentHorizontal.set(horizontal)
        contentAlignmentVertical.set(vertical)
    }

}