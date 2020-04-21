package net.apptronic.core.threading

@Deprecated("Should use coroutines")
interface Action {

    fun execute()

}

@Deprecated("Should use coroutines")
fun lambdaAction(action: () -> Unit): Action {
    return LambdaAction(action)
}

@Deprecated("Should use coroutines")
private class LambdaAction(
        private val action: () -> Unit
) : Action {
    override fun execute() {
        action.invoke()
    }
}