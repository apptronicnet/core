package net.apptronic.common.android.ui.components.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import net.apptronic.common.android.ui.components.fragment.FragmentLifecycle
import net.apptronic.common.android.ui.components.fragment.FragmentViewModel
import net.apptronic.common.android.ui.threading.AndroidActivityMainThreadExecutor
import net.apptronic.common.android.ui.threading.ThreadExecutor
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleHolder

abstract class BaseActivity<Model : FragmentViewModel> : AppCompatActivity(), LifecycleHolder<FragmentLifecycle> {

    private val lifecycle = FragmentLifecycle()
    private val threadExecutor = AndroidActivityMainThreadExecutor(this)

    override fun localLifecycle(): FragmentLifecycle = lifecycle

    override fun threadExecutor(): ThreadExecutor = threadExecutor

    private var model: Model? = null

    fun setModel(model: Model) {
        this.model = model
        onBindModel(model)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        lifecycle.createdStage.enter()
        super.onCreate(savedInstanceState)
    }

    open fun onBindModel(model: Model) {
        model.context.set(this)
    }

    override fun onStart() {
        lifecycle.startedStage.enter()
        super.onStart()
    }

    override fun onResume() {
        lifecycle.resumedStage.enter()
        super.onResume()
    }

    override fun onPause() {
        lifecycle.resumedStage.exit()
        super.onPause()
    }

    override fun onStop() {
        lifecycle.startedStage.exit()
        super.onStop()
    }

    override fun onDestroy() {
        lifecycle.createdStage.exit()
        super.onDestroy()
    }

}