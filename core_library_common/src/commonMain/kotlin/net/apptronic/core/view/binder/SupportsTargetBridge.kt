package net.apptronic.core.view.binder

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.subject.Subject

interface SupportsTargetBridge<T> {

    fun <E : TargetBridge<T>> addBridge(bridge: E): E

    fun <E> readObservable(bridger: T.() -> Observable<E>): Observable<E> {
        return addBridge(ObservableBridge<T, E, E>(bridger, { it }))
    }

    fun <S, E> readObservable(bridger: T.() -> Observable<S>, function: (S) -> E): Observable<E> {
        return addBridge(ObservableBridge<T, S, E>(bridger, function))
    }

    fun <E> readValue(bridger: T.() -> E): Observable<E> {
        return addBridge(ValueBridge(bridger, { it }))
    }

    fun <S, E> readValue(bridger: T.() -> S, function: (S) -> E): Observable<E> {
        return addBridge(ValueBridge(bridger, function))
    }

    fun <E> writeSubject(bridger: T.() -> Subject<E>): Subject<E> {
        return addBridge(SubjectBridge(bridger))
    }

    fun <E> writeInvocation(bridger: T.() -> E): () -> E {
        return addBridge(InvocationBridge(bridger))::invoke
    }

    fun <Param, E> writeInvocation(bridger: T.(Param) -> E): (Param) -> E {
        return addBridge(ParamInvocationBridge(bridger))::invoke
    }

}