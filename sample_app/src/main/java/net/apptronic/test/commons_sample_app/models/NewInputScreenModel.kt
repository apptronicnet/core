package net.apptronic.test.commons_sample_app.models

import android.provider.SyncStateContract.Helpers.update
import net.apptronic.core.component.entity.functions.variants.value
import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.test.commons_sample_app.ToolbarTitled

class NewInputScreenModel(
    lifecycle: Lifecycle,
    private val resultListener: ResultListener<String>
) : FragmentViewModel(lifecycle), ToolbarTitled {

    val newInput = TextInputModel(this)

    val submitBtnClicked = genericEvent().setup {
        subscribe {
            processWithProgress()
        }
    }

    val isProgressBarVisible = value(false)
    val progressBarCountDown = value<Int>()

    override fun getToolbarTitle(): String {
        return "New input"
    }

    private fun processWithProgress() {
        isProgressBarVisible.set(true)
        var count = 8
        Thread(Runnable {
            while (count > 0) {
                progressBarCountDown.set(count)
                Thread.sleep(1000)
                count--
            }
            update {
                isProgressBarVisible.set(false)
                resultListener.setResult(newInput.getText())
                closeSelf()
            }
        }).start()
    }

}