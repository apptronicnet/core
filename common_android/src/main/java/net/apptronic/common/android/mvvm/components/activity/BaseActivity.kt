package net.apptronic.common.android.mvvm.components.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import net.apptronic.common.android.mvvm.components.fragment.FragmentLifecycle
import net.apptronic.common.core.mvvm.viewmodel.ViewModel
import net.apptronic.common.core.mvvm.viewmodel.ViewModelRegistry
import net.apptronic.common.core.mvvm.viewmodel.lifecycle.enterStage
import net.apptronic.common.core.mvvm.viewmodel.lifecycle.exitStage

abstract class BaseActivity<Model : ViewModel> : AppCompatActivity() {

    companion object {
        const val VIEW_MODEL_ID = "_view_model_id"
    }

    private var viewModelId: Long = ViewModelRegistry.NO_ID

    val model: Model by ViewModelRegistry.obtain {
        if (viewModelId == ViewModelRegistry.NO_ID) {
            viewModelId = createViewModel()!!.apply {
                ViewModelRegistry.add(this)
            }.getId()
        }
        viewModelId
    }

    /**
     * Creates [ViewModel] for this [BaseActivity]
     */
    open fun createViewModel(): Model? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (intent.hasExtra(VIEW_MODEL_ID)) {
            viewModelId = intent.getLongExtra(VIEW_MODEL_ID, ViewModelRegistry.NO_ID)
        } else savedInstanceState?.let {
            if (it.containsKey(VIEW_MODEL_ID)) {
                viewModelId = savedInstanceState.getLong(VIEW_MODEL_ID)
            }
        }
        enterStage(model, FragmentLifecycle.STAGE_CREATED)
        super.onCreate(null)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putLong(VIEW_MODEL_ID, viewModelId)
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

    override fun onDestroy() {
        exitStage(model, FragmentLifecycle.STAGE_CREATED)
        super.onDestroy()
        if (isFinishing) {
            model.finishLifecycle()
        }
    }

}