package net.apptronic.common.android.ui.viewmodel.lifecycle

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.concurrent.atomic.AtomicBoolean

class LifecycleStage(val name: String) {

    private val isEntered = AtomicBoolean(false)
    private val enter = LifecycleEvent()
    private val exit = LifecycleEvent()

    fun isEntered() = isEntered.get()

    private var disposables: CompositeDisposable? = null

    fun enter() {
        disposables = CompositeDisposable()
        isEntered.set(true)
        enter.notifyListeners()
    }

    fun exit() {
        isEntered.set(false)
        disposables?.dispose()
        disposables = null
        exit.notifyListeners()
    }

    override fun toString(): String {
        return "LifecycleStage: $name"
    }

    fun subscribeEnter(callback: OnEnterHandler.() -> Unit) {
        subscribeEnter(object : LifecycleEvent.Listener {
            override fun onEvent(event: LifecycleEvent) {
                OnEnterHandler().callback()
            }
        })
    }

    fun subscribeExit(callback: OnExitHandler.() -> Unit) {
        subscribeExit(object : LifecycleEvent.Listener {
            override fun onEvent(event: LifecycleEvent) {
                OnExitHandler().callback()
            }
        })
    }

    fun subscribeEnter(listener: LifecycleEvent.Listener) {
        enter.subscribe(listener)
        if (isEntered.get()) {
            listener.onEvent(enter)
        }
    }

    fun subscribeExit(listener: LifecycleEvent.Listener) {
        exit.subscribe(listener)
    }

    inner class OnEnterHandler {

        fun Disposable.disposeOnExit() {
            disposables?.add(this) ?: dispose()
        }

        fun onExit(callback: OnExitHandler.() -> Unit) {
            subscribeExit(callback)
        }

    }

    inner class OnExitHandler {

    }

}