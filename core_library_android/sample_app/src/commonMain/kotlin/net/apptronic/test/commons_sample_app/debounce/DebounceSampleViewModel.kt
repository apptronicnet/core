package net.apptronic.test.commons_sample_app.debounce

import net.apptronic.core.base.concurrent.AtomicEntity
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.behavior.debounce
import net.apptronic.core.component.entity.entities.setTo
import net.apptronic.core.component.entity.functions.map
import net.apptronic.core.component.entity.functions.mapOr
import net.apptronic.core.component.entity.functions.onNext
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.platform.pauseCurrentThread

fun createDebounceSampleViewModel(parent: Context): DebounceSampleViewModel {
    return DebounceSampleViewModel(ViewModelContext(parent))
}

class DebounceSampleViewModel(context: ViewModelContext) : ViewModel(context) {

    private val serialGenerator = AtomicEntity(1)

    val onClickEmitNewItem = genericEvent() {
        currentItemIndex.set(
            Source(serialGenerator.perform {
                val current = get()
                set(get() + 1)
                return@perform current
            })
        )
    }

    private val currentItemIndex = value<Source>()
    private val processingItemIndex = value<Processing?>(null)
    private val processingTimer = value<Int?>(null)
    private val resultItemIndex = value<Result>()

    val currentItem = currentItemIndex.map { it.toString() }
    val timer = processingTimer.mapOr("-") { it.toString() }
    val processingItem = processingItemIndex.mapOr("") { it.toString() }
    val resultItem = resultItemIndex.map { it.toString() }

    init {
        currentItemIndex.debounce { source ->
            source.onNext { processingItemIndex.set(Processing(it.index)) }
                .map {
                    var remaining = 10
                    while (remaining > 0) {
                        processingTimer.set(remaining)
                        pauseCurrentThread(300)
                        remaining--
                    }
                    processingTimer.set(null)
                    Result(it.index)
                }.onNext {
                    processingItemIndex.set(null)
                }
        }.setTo(resultItemIndex)
    }

    private class Source(val index: Int) {
        override fun toString(): String {
            return "Source $index"
        }
    }

    private class Processing(val index: Int) {
        override fun toString(): String {
            return "Processing $index"
        }
    }

    private class Result(val index: Int) {
        override fun toString(): String {
            return "Result $index"
        }
    }

}