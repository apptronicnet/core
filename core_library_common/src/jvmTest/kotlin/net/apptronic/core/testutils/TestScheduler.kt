package net.apptronic.core.testutils

import net.apptronic.core.component.context.Context
import net.apptronic.core.threading.ContextScheduler
import net.apptronic.core.threading.Scheduler

fun createTestScheduler(context: Context): Scheduler {
    return ContextScheduler(context)
}