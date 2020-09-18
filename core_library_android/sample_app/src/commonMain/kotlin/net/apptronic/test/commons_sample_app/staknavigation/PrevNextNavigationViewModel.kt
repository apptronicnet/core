package net.apptronic.test.commons_sample_app.staknavigation

import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.context.viewModelContext
import net.apptronic.core.component.entity.operators.increment
import net.apptronic.core.component.toggle
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.mvvm.viewmodel.adapter.BasicTransition
import net.apptronic.core.mvvm.viewmodel.navigation.stackNavigator

fun Contextual.prevNextNavigationViewModel() = PrevNextNavigationViewModel(viewModelContext())

class PrevNextNavigationViewModel internal constructor(
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
        navigator.add(nextViewModel(), BasicTransition.Next)
    }

    fun onClickBack() {
        navigator.popBackStack(BasicTransition.Previous)
    }

    fun onClickReplace() {
        navigator.replace(nextViewModel(), BasicTransition.Fade)
    }

    fun onClickReplaceAll() {
        navigator.replaceAll(nextViewModel(), BasicTransition.Fade)
    }

}