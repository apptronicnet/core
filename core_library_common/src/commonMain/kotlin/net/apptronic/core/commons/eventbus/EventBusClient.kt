package net.apptronic.core.commons.eventbus

import net.apptronic.core.entity.base.SubjectEntity

interface EventBusClient<T> : SubjectEntity<T> {

    fun postEvent(event: T)

}