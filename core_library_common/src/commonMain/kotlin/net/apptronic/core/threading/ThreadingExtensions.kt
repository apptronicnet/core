package net.apptronic.core.threading

@Deprecated("Should use coroutines")
fun Worker.execute(action: () -> Unit) {
    execute(lambdaAction(action))
}