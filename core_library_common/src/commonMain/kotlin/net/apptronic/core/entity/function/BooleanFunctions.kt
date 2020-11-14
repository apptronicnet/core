package net.apptronic.core.entity.function

import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.Property

fun <T> Entity<T>.isNull(): Property<Boolean> =
        entityFunction(this) {
            it == null
        }

fun <T> Entity<T>.isNotNull(): Property<Boolean> =
        entityFunction(this) {
            it != null
        }

/**
 * Function which emits single true signal when source entity emits any item. Good for usage when needed to
 * observe when entity without value receives it's first value
 */
fun <T> Entity<T>.anyValue(): Property<Boolean> =
        entityFunction(this) {
            true
        }

fun Entity<Boolean>.isTrue(): Property<Boolean> =
        entityFunction(this) {
            it
        }

fun Entity<Boolean>.isFalse(): Property<Boolean> =
        entityFunction(this) {
            it.not()
        }

fun Entity<Boolean>.not(): Property<Boolean> =
        entityFunction(this) {
            it.not()
        }

fun Entity<Boolean?>.isTrueOrNull(): Property<Boolean> =
        entityFunction(this) {
            it == null || it
        }

fun Entity<Boolean?>.isFalseOrNull(): Property<Boolean> =
        entityFunction(this) {
            it == null || it.not()
        }

infix fun <A, B> Entity<A>.isEqualsTo(another: Entity<B>): Property<Boolean> =
        entityFunction(this, another) { left, right ->
            left == right
        }

infix fun <A, B> Entity<A>.isEqualsTo(another: B): Property<Boolean> =
        entityFunction(this) {
            it == another
        }

infix fun <A, B> Entity<A>.isNotEqualsTo(another: Entity<B>): Property<Boolean> =
        entityFunction(this, another) { left, right ->
            left != right
        }

infix fun <A, B> Entity<A>.isNotEqualsTo(another: B): Property<Boolean> =
        entityFunction(this) {
            it != another
        }

infix fun Entity<Boolean>.and(another: Entity<Boolean>): Property<Boolean> =
        entityFunction(this, another) { left, right ->
            left and right
        }

infix fun Entity<Boolean>.or(another: Entity<Boolean>): Property<Boolean> =
        entityFunction(this, another) { left, right ->
            left or right
        }

infix fun Entity<Boolean>.xor(another: Entity<Boolean>): Property<Boolean> =
        entityFunction(this, another) { left, right ->
            left xor right
        }

fun <T : CharSequence> Entity<T>.isEmpty(): Property<Boolean> =
        entityFunction(this) {
            it.isEmpty()
        }

fun <T : CharSequence> Entity<T>.isNotEmpty(): Property<Boolean> =
        entityFunction(this) {
            it.isNotEmpty()
        }

fun <T : CharSequence> Entity<T>.isBlank(): Property<Boolean> =
        entityFunction(this) {
            it.isBlank()
        }

fun <T : CharSequence> Entity<T>.isNotBlank(): Property<Boolean> =
        entityFunction(this) {
            it.isNotBlank()
        }

fun <T> allOf(
        vararg entity: Entity<out T>,
        transformation: (Entity<out T>) -> Entity<Boolean>
): Property<Boolean> {
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
): Property<Boolean> {
    val sources = entity.map { it as Entity<*> }.toTypedArray()
    return entityArrayFunction(sources) { args ->
        args.all { transformation.invoke(it as T) }
    }
}

fun <T> anyOf(
        vararg entity: Entity<out T>,
        transformation: (Entity<out T>) -> Entity<Boolean>
): Property<Boolean> {
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
): Property<Boolean> {
    val sources = entity.map { it as Entity<*> }.toTypedArray()
    return entityArrayFunction(sources) { args ->
        args.any { transformation.invoke(it as T) }
    }
}

fun <T> noneOf(
        vararg entity: Entity<out T>,
        transformation: (Entity<out T>) -> Entity<Boolean>
): Property<Boolean> {
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
): Property<Boolean> {
    val sources = entity.map { it as Entity<*> }.toTypedArray()
    return entityArrayFunction(sources) { args ->
        args.none { transformation.invoke(it as T) }
    }
}
