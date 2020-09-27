package net.apptronic.core.view.binder.target

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.subject.Subject

interface SupportsTargetBridge<T> {

    fun <E : TargetBridge<T>> addBridge(bridge: E): E

}

inline fun <T, reified E> SupportsTargetBridge<T>.bindFrom(
        noinline bridger: T.() -> Observable<E>
): Observable<E> {
    return addBridge(ObservableBridge<T, E, E>(bridger, { it }))
}

inline fun <T, reified S, reified E> SupportsTargetBridge<T>.bindFrom(
        noinline bridger: T.() -> Observable<S>,
        noinline function: (S) -> E
): Observable<E> {
    return addBridge(ObservableBridge<T, S, E>(bridger, function))
}

inline fun <T, reified E> SupportsTargetBridge<T>.bindValue(
        noinline bridger: T.() -> E
): Observable<E> {
    return addBridge(ValueBridge(bridger, { it }))
}

inline fun <T, reified S, reified E> SupportsTargetBridge<T>.bindValue(
        noinline bridger: T.() -> S,
        noinline function: (S) -> E
): Observable<E> {
    return addBridge(ValueBridge(bridger, function))
}

inline fun <T, reified E> SupportsTargetBridge<T>.bindTo(
        noinline bridger: T.() -> Subject<E>
): Subject<E> {
    return addBridge(SubjectBridge(bridger))
}

inline fun <T, reified E> SupportsTargetBridge<T>.bindTo(
        noinline bridger: T.() -> E
): () -> E {
    return addBridge(InvocationBridge(bridger))::invoke
}

inline fun <T, reified Param, reified E> SupportsTargetBridge<T>.bindTo(
        noinline bridger: T.(Param) -> E
): (Param) -> E {
    return addBridge(ParamInvocationBridge(bridger))::invoke
}

