package net.apptronic.shoppinglist.uisample

import androidx.core.content.ContextCompat
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import net.apptronic.common.android.ui.components.fragment.FragmentLifecycle
import net.apptronic.common.android.ui.components.fragment.FragmentViewModel
import net.apptronic.common.android.ui.components.submodels.TextViewModel
import net.apptronic.common.android.ui.viewmodel.entity.assignAsFunctionFrom
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleHolder
import net.apptronic.shoppinglist.R
import java.util.concurrent.TimeUnit

/**
 * Represents sample screen properties, state and events
 */
class SampleViewModel(lifecycleHolder: LifecycleHolder<FragmentLifecycle>) : FragmentViewModel(lifecycleHolder) {

    val title = value<String>()
    val onClickRefreshTitle = genericEvent()
    val userInputUpdates = typedEvent<String>()
    val userInputValue = value<String>()
    val onClickConfirmInputEvent = genericEvent()
    val currentInputText = value<String>()
    val secondCounter = value<String>()
    val toastOnPause = genericEvent()

    val confirmedInputText = TextViewModel(this).apply {
        textColor.assignAsFunctionFrom(context, text, userInputValue) { context, text, userInputValue ->
            if (text == userInputValue) {
                ContextCompat.getColor(context, R.color.blueText)
            } else {
                ContextCompat.getColor(context, R.color.redText)
            }
        }
    }

    init {

        var titleChanges = 0
        var seconds = 0

        title.set("Another title")

        onCreate {

            onExit {

            }
        }

        onViewCreated {
            onClickRefreshTitle.subscribe {
                titleChanges++
                title.set("Title changes $titleChanges")
            }
        }

        onResume {
            userInputValue.subscribe {
                currentInputText.set("Test: $it")
            }
            onClickConfirmInputEvent.subscribe {
                confirmedInputText.text.set(userInputValue)
            }
        }

        onResume {
            Observable.interval(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                    .subscribe {
                        secondCounter.set("[${seconds++}]")
                    }.disposeOnExit()
            onExit {
                toastOnPause.sendEvent()
            }

        }

    }

}

