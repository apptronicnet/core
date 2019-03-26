package net.apptronic.core.component.process

interface BackgroundAction<T, R> {

    fun execute(request: T): R

}