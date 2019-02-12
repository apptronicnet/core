package net.apptronic.test.commons_sample_app.models

import net.apptronic.common.android.mvvm.components.fragment.FragmentViewModel
import net.apptronic.common.core.component.lifecycle.Lifecycle

class YesNoSelectorViewModel(
    lifecycle: Lifecycle,
    private val resultListener: ResultListener<String>
) : FragmentViewModel(lifecycle) {

    val onClickBtnYes = genericEvent().setup {
        subscribe {
            sendResult("You clicked YES")
        }
    }

    val onClickBtnNo = genericEvent().setup {
        subscribe {
            sendResult("You clicked NO")
        }
    }

    val onClickBtnMaybe = genericEvent().setup {
        subscribe {
            sendResult("You clicked MAYBE (Определись!!!)")
        }
    }

    private fun sendResult(text: String) {
        Thread(Runnable {
            Thread.sleep(3000)
            update {
                resultListener.setResult(text)
                closeSelf()
            }
        }).start()
    }

}