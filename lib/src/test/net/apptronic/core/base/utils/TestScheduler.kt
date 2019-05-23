package net.apptronic.core.base.utils

import net.apptronic.core.threading.ContextScheduler
import net.apptronic.core.threading.Scheduler

fun createTestScheduler(): Scheduler {
    return ContextScheduler(null)
}