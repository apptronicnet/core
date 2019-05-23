package net.apptronic.core.mvvm.viewmodel.extensions

import net.apptronic.core.base.SubscriptionHolder
import net.apptronic.core.base.SubscriptionHolders
import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.component.entity.entities.Property

fun forEachChangeAnyOf(vararg properties: Property<*>, action: () -> Unit): SubscriptionHolder {
    val subscriptionHolders = SubscriptionHolders()
    properties.forEach { property ->
        property.subscribe { _ ->
            ifAllIsSet(*properties) {
                action()
            }
        }
    }
    return subscriptionHolders
}

fun <T> Property<T>.copyValueFrom(source: Property<T>): Property<T> {
    source.subscribe { set(it) }
    return this
}