package net.apptronic.core.base.observable

class BasicSubscription<T>(private val target: Observer<T>) : Observer<T>, Subscription {

    private var isUnsubscribed = false
    private val unsubscribeActions = mutableListOf<() -> Unit>()

    override fun isUnsubscribed(): Boolean {
        return isUnsubscribed
    }

    override fun update(value: T) {
        if (!isUnsubscribed) {
            target.update(value)
        }
    }

    override fun unsubscribe() {
        isUnsubscribed = true
        unsubscribeActions.forEach {
            it.invoke()
        }
    }

    fun doOnUnsubscribe(action: () -> Unit) {
        if (isUnsubscribed) {
            action.invoke()
        } else {
            unsubscribeActions.add(action)
        }
    }

}