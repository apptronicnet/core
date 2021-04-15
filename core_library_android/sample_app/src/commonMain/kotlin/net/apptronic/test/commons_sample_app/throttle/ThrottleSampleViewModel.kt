package net.apptronic.test.commons_sample_app.throttle

import kotlinx.coroutines.delay
import net.apptronic.core.base.SerialIdGenerator
import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.childContext
import net.apptronic.core.entity.behavior.throttleMap
import net.apptronic.core.entity.commons.genericEvent
import net.apptronic.core.entity.commons.setTo
import net.apptronic.core.entity.commons.value
import net.apptronic.core.entity.function.map
import net.apptronic.core.entity.function.mapOr
import net.apptronic.core.viewmodel.ViewModel

fun Contextual.throttleSampleViewModel() = ThrottleSampleViewModel(childContext())

class ThrottleSampleViewModel internal constructor(context: Context) : ViewModel(context) {

    private val serialGenerator = SerialIdGenerator()

    val onClickEmitNewItem = genericEvent {
        currentItemIndex.set(
            Source(serialGenerator.nextId().toInt())
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
        currentItemIndex.throttleMap {
            processingItemIndex.set(Processing(it.index))
            var remaining = 10
            while (remaining > 0) {
                processingTimer.set(remaining)
                delay(300)
                remaining--
            }
            processingTimer.set(null)
            processingItemIndex.set(null)
            Result(it.index)
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