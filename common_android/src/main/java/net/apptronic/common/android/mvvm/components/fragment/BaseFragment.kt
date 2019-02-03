package net.apptronic.common.android.mvvm.components.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import net.apptronic.common.core.component.Component
import net.apptronic.common.core.component.lifecycle.enterStage
import net.apptronic.common.core.component.lifecycle.exitStage
import net.apptronic.common.core.mvvm.viewmodel.ComponentRegistry

abstract class BaseFragment<Model : Component> : Fragment(), ViewModelController {

    companion object {
        const val VIEW_MODEL_ID = "_view_model_id"
    }

    val model: Model by ComponentRegistry.obtain {
        arguments!!.getLong(VIEW_MODEL_ID, -1)
    }

    override fun setViewModel(id: Long) {
        (arguments ?: Bundle().also {
            arguments = it
        }).apply {
            putLong(VIEW_MODEL_ID, id)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enterStage(model, FragmentLifecycle.STAGE_CREATED)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        enterStage(model, FragmentLifecycle.STAGE_VIEW_CREATED)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        enterStage(model, FragmentLifecycle.STAGE_STARTED)
        super.onStart()
    }

    override fun onResume() {
        enterStage(model, FragmentLifecycle.STAGE_RESUMED)
        super.onResume()
    }

    override fun onPause() {
        exitStage(model, FragmentLifecycle.STAGE_RESUMED)
        super.onPause()
    }

    override fun onStop() {
        exitStage(model, FragmentLifecycle.STAGE_STARTED)
        super.onStop()
    }

    override fun onDestroyView() {
        exitStage(model, FragmentLifecycle.STAGE_VIEW_CREATED)
        super.onDestroyView()
    }

    override fun onDestroy() {
        exitStage(model, FragmentLifecycle.STAGE_CREATED)
        super.onDestroy()
    }

}