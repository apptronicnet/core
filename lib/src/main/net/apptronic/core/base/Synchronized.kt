package net.apptronic.core.base

expect class Synchronized() {

    fun <R> run(block: () -> R): R

}