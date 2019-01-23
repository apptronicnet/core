package net.apptronic.common.android.ui.viewmodel.lifecycle

import net.apptronic.common.android.ui.threading.ThreadExecutorProvider

class GenericLifecycle(provider: ThreadExecutorProvider) : Lifecycle(provider) {

    val stage1 = createStage("Stage 1")
    val stage2 = createStage("Stage 2")
    val stage3 = createStage("Stage 3")

}