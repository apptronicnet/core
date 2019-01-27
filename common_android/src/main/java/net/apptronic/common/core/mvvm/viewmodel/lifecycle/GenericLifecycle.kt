package net.apptronic.common.core.mvvm.viewmodel.lifecycle

import net.apptronic.common.core.mvvm.threading.ThreadExecutorProvider

class GenericLifecycle(provider: ThreadExecutorProvider) : Lifecycle(provider) {

    val stage1 = createStage("Stage 1")
    val stage2 = createStage("Stage 2")
    val stage3 = createStage("Stage 3")

}