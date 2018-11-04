package net.apptronic.common.android.ui.components.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import net.apptronic.common.android.ui.threading.AndroidFragmentMainThreadExecutor
import net.apptronic.common.android.ui.threading.ThreadExecutor
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleHolder

abstract class BaseFragment<ViewModel : FragmentViewModel> : Fragment(), LifecycleHolder<FragmentLifecycle> {

    private val lifecycle = FragmentLifecycle()
    private val threadExecutor = AndroidFragmentMainThreadExecutor(this)

    override fun localLifecycle(): FragmentLifecycle = lifecycle

    override fun threadExecutor(): ThreadExecutor = threadExecutor

    private var model: ViewModel? = null

    fun setModel(model: ViewModel) {
        context?.let {
            model.context.set(it)
        }
        this.model = model
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        lifecycle.createdStage.enter()
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycle.viewCreatedStage.enter()
        super.onViewCreated(view, savedInstanceState)
        model?.let {
            onBindModel(view, it)
        }
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

    open fun onBindModel(view: View, model: ViewModel) {
        model.context.set(view.context)
    }

}