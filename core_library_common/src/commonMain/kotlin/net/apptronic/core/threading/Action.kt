package net.apptronic.core.threading

interface Action {

    fun execute()

}

fun lambdaAction(action: () -> Unit): Action {
    return LambdaAction(action)
}

private class LambdaAction(
        private val action: () -> Unit
) : Action {
    override fun execute() {
        action.invoke()
    }
}