package net.apptronic.core.commons.eventbus

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.entity.base.SubjectEntity

@UnderDevelopment
interface EventBusClient<T> : SubjectEntity<T> {

    fun postEvent(event: T)

}