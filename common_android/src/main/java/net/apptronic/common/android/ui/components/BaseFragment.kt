package net.apptronic.common.android.ui.components

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import net.apptronic.common.android.ui.threading.AndroidFragmentMainThreadExecutor
import net.apptronic.common.android.ui.threading.ThreadExecutor
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleHolder

abstract class BaseFragment<Model : FragmentViewModel> : Fragment(), LifecycleHolder<FragmentLifecycle> {

    private val lifecycle = FragmentLifecycle()
    private val threadExecutor = AndroidFragmentMainThreadExecutor(this)

    override fun localLifecycle(): FragmentLifecycle = lifecycle

    override fun threadExecutor(): ThreadExecutor = threadExecutor

    abstract fun onCreateModel(): Model

    private var model: Model? = null

    fun model(): Model {
        val model = this.model
        if (model != null) {
            return model
        } else {
            throw IllegalStateException("Model is not initialized")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        model = onCreateModel()
        lifecycle.createdStage.enter()
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycle.viewCreatedStage.enter()
        super.onViewCreated(view, savedInstanceState)
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

    override fun onDestroyView() {
        lifecycle.viewCreatedStage.exit()
        super.onDestroyView()
    }

    override fun onDestroy() {
        lifecycle.createdStage.exit()
        super.onDestroy()
    }

}