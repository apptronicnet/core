package net.apptronic.test.commons_sample_app.staknavigation

import net.apptronic.core.context.Contextual
import net.apptronic.core.entity.commons.toggle
import net.apptronic.core.entity.commons.value
import net.apptronic.core.entity.operators.increment
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.ViewModelContext
import net.apptronic.core.viewmodel.navigation.BasicTransition
import net.apptronic.core.viewmodel.navigation.stackNavigator
import net.apptronic.core.viewmodel.viewModelContext

fun Contextual.gestureNavigationViewModel() = GestureNavigationViewModel(viewModelContext())

class GestureNavigationViewModel internal constructor(
    context: ViewModelContext
) : ViewModel(context) {

    private val index = value<Int>(1)
    private val nextColor = toggle(*StackItemColor.values())

    val navigator = stackNavigator()

    init {
        nextColor.toggleTo(StackItemColor.values()[0])
        navigator.set(nextViewModel())
    }

    private fun nextViewModel(): ViewModel {
        val color = nextColor.get()
        val index = index.get()
        this.index.increment()
        nextColor.toggle()
        return StackItemViewModel(context, index = index, backgroundColor = color)
    }

    fun onClickAdd() {
        navigator.add(nextViewModel(), BasicTransition.Forward)
    }

    fun onClickBack() {
        navigator.popBackStack(BasicTransition.Backward)
    }

    fun onClickReplace() {
        navigator.replace(nextViewModel(), BasicTransition.Fade)
    }

    fun onClickReplaceAll() {
        navigator.replaceAll(nextViewModel(), BasicTransition.Fade)
    }

}