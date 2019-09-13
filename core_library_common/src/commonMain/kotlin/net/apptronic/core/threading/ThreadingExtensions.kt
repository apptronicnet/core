package net.apptronic.core.threading

fun Worker.execute(action: () -> Unit) {
    execute(lambdaAction(action))
}