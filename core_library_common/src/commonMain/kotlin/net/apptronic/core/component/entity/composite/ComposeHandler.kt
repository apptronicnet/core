package net.apptronic.core.component.entity.composite

import net.apptronic.core.base.collections.Queue
import net.apptronic.core.base.observable.subject.Subject

class ComposedNext<E>(val value: E, val index: Int)

interface ComposeHandler<E, T> {

    fun compose(queue: Queue<ComposedNext<E>>, count: Int): Subject<T>

}