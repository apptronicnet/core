package net.apptronic.core.entity.function

import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.FunctionProperty

fun <T> Entity<T>.isNull(): FunctionProperty<Boolean> =
        entityFunction(this) {
            it == null
        }

fun <T> Entity<T>.isNotNull(): FunctionProperty<Boolean> =
        entityFunction(this) {
            it != null
        }

/**
 * Function which emits single true signal when source entity emits any item. Good for usage when needed to
 * observe when entity without value receives it's first value
 */
fun <T> Entity<T>.anyValue(): FunctionProperty<Boolean> =
        entityFunction(this) {
            true
        }

fun Entity<Boolean>.isTrue(): FunctionProperty<Boolean> =
        entityFunction(this) {
            it
        }

fun Entity<Boolean>.isFalse(): FunctionProperty<Boolean> =
        entityFunction(this) {
            it.not()
        }

fun Entity<Boolean>.not(): FunctionProperty<Boolean> =
        entityFunction(this) {
            it.not()
        }

fun Entity<Boolean?>.isTrueOrNull(): FunctionProperty<Boolean> =
        entityFunction(this) {
            it == null || it
        }

fun Entity<Boolean?>.isFalseOrNull(): FunctionProperty<Boolean> =
        entityFunction(this) {
            it == null || it.not()
        }

infix fun <A, B> Entity<A>.isEqualsTo(another: Entity<B>): FunctionProperty<Boolean> =
        entityFunction(this, another) { left, right ->
            left == right
        }

infix fun <A, B> Entity<A>.isEqualsTo(another: B): FunctionProperty<Boolean> =
        entityFunction(this) {
            it == another
        }

infix fun <A, B> Entity<A>.isNotEqualsTo(another: Entity<B>): FunctionProperty<Boolean> =
        entityFunction(this, another) { left, right ->
            left != right
        }

infix fun <A, B> Entity<A>.isNotEqualsTo(another: B): FunctionProperty<Boolean> =
        entityFunction(this) {
            it != another
        }

infix fun Entity<Boolean>.and(another: Entity<Boolean>): FunctionProperty<Boolean> =
        entityFunction(this, another) { left, right ->
            left and right
        }

infix fun Entity<Boolean>.or(another: Entity<Boolean>): FunctionProperty<Boolean> =
        entityFunction(this, another) { left, right ->
            left or right
        }

infix fun Entity<Boolean>.xor(another: Entity<Boolean>): FunctionProperty<Boolean> =
        entityFunction(this, another) { left, right ->
            left xor right
        }

fun <T : CharSequence> Entity<T>.isEmpty(): FunctionProperty<Boolean> =
        entityFunction(this) {
            it.isEmpty()
        }

fun <T : CharSequence> Entity<T>.isNotEmpty(): FunctionProperty<Boolean> =
        entityFunction(this) {
            it.isNotEmpty()
        }

fun <T : CharSequence> Entity<T>.isBlank(): FunctionProperty<Boolean> =
        entityFunction(this) {
            it.isBlank()
        }

fun <T : CharSequence> Entity<T>.isNotBlank(): FunctionProperty<Boolean> =
        entityFunction(this) {
            it.isNotBlank()
        }

fun <T> allOf(
        vararg entity: Entity<out T>,
        transformation: (Entity<out T>) -> Entity<Boolean>
): FunctionProperty<Boolean> {
    val sources = entity.map {
        transformation.invoke(it)
    }.toTypedArray<Entity<*>>()
    return entityArrayFunction(sources) { args ->
        args.all { it as Boolean }
    }
}

@Suppress("UNCHECKED_CAST")
fun <T> allOfValues(
        vararg entity: Entity<out T>,
        transformation: (T) -> Boolean
): FunctionProperty<Boolean> {
    val sources = entity.map { it as Entity<*> }.toTypedArray()
    return entityArrayFunction(sources) { args ->
        args.all { transformation.invoke(it as T) }
    }
}

fun <T> anyOf(
        vararg entity: Entity<out T>,
        transformation: (Entity<out T>) -> Entity<Boolean>
): FunctionProperty<Boolean> {
    val sources = entity.map {
        transformation.invoke(it)
    }.toTypedArray<Entity<*>>()
    return entityArrayFunction(sources) { args ->
        args.any { it as Boolean }
    }
}

@Suppress("UNCHECKED_CAST")
fun <T> anyOfValues(
        vararg entity: Entity<out T>,
        transformation: (T) -> Boolean
): FunctionProperty<Boolean> {
    val sources = entity.map { it as Entity<*> }.toTypedArray()
    return entityArrayFunction(sources) { args ->
        args.any { transformation.invoke(it as T) }
    }
}

fun <T> noneOf(
        vararg entity: Entity<out T>,
        transformation: (Entity<out T>) -> Entity<Boolean>
): FunctionProperty<Boolean> {
    val sources = entity.map {
        transformation.invoke(it)
    }.toTypedArray<Entity<*>>()
    return entityArrayFunction(sources) { args ->
        args.none { it as Boolean }
    }
}

@Suppress("UNCHECKED_CAST")
fun <T> noneOfValues(
        vararg entity: Entity<out T>,
        transformation: (T) -> Boolean
): FunctionProperty<Boolean> {
    val sources = entity.map { it as Entity<*> }.toTypedArray()
    return entityArrayFunction(sources) { args ->
        args.none { transformation.invoke(it as T) }
    }
}
