package net.apptronic.test.commons_sample_app.staknavigation

import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.childContext
import net.apptronic.core.entity.commons.toggle
import net.apptronic.core.entity.commons.value
import net.apptronic.core.entity.operators.increment
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.navigation.BasicTransition
import net.apptronic.core.viewmodel.navigation.stackNavigator

fun Contextual.prevNextNavigationViewModel() = PrevNextNavigationViewModel(childContext())

class PrevNextNavigationViewModel internal constructor(context: Context) : ViewModel(context) {

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
        return stackItemViewModel(index = index, backgroundColor = color)
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