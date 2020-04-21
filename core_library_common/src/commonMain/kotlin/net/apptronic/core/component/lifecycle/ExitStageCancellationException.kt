package net.apptronic.core.component.lifecycle

import kotlinx.coroutines.CancellationException

class ExitStageCancellationException(val stageName: String) : CancellationException("Stage $stageName exited")