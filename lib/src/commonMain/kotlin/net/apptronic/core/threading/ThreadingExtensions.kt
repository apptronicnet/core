package net.apptronic.core.threading

fun Worker.execute(action: () -> Unit) {
    execute(LambdaAction(action))
}

private class LambdaAction(
    private val action: () -> Unit
) : Action {
    override fun execute() {
        action.invoke()
    }
}