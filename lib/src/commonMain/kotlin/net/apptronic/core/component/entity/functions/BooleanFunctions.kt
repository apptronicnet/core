package net.apptronic.core.component.entity.functions

import net.apptronic.core.component.entity.Entity

fun <T> Entity<T>.isNull(): Function<Boolean> =
        entityFunction(this) {
            it == null
        }

fun <T> Entity<T>.isNotNull(): Function<Boolean> =
        entityFunction(this) {
            it != null
        }

fun <T> Entity<T>.anyValue(): Function<Boolean> =
        entityFunction(this) {
            true
        }

fun Entity<Boolean>.isTrue(): Function<Boolean> =
        entityFunction(this) {
            it
        }

fun Entity<Boolean>.isFalse(): Function<Boolean> =
        entityFunction(this) {
            it.not()
        }

fun Entity<Boolean>.not(): Function<Boolean> =
        entityFunction(this) {
            it.not()
        }

fun Entity<Boolean?>.isTrueOrNull(): Function<Boolean> =
        entityFunction(this) {
            it == null || it
        }

fun Entity<Boolean?>.isFalseOrNull(): Function<Boolean> =
        entityFunction(this) {
            it == null || it.not()
        }

infix fun <A, B> Entity<A>.isEqualsTo(another: Entity<B>): Function<Boolean> =
        entityFunction(this, another) { left, right ->
            left == right
        }

infix fun <A, B> Entity<A>.isEqualsTo(another: B): Function<Boolean> =
        entityFunction(this) {
            it == another
        }

infix fun <A, B> Entity<A>.isNotEqualsTo(another: Entity<B>): Function<Boolean> =
        entityFunction(this, another) { left, right ->
            left != right
        }

infix fun <A, B> Entity<A>.isNotEqualsTo(another: B): Function<Boolean> =
        entityFunction(this) {
            it != another
        }

infix fun Entity<Boolean>.and(another: Entity<Boolean>): Function<Boolean> =
        entityFunction(this, another) { left, right ->
            left and right
        }

infix fun Entity<Boolean>.or(another: Entity<Boolean>): Function<Boolean> =
        entityFunction(this, another) { left, right ->
            left or right
        }

infix fun Entity<Boolean>.xor(another: Entity<Boolean>): Function<Boolean> =
        entityFunction(this, another) { left, right ->
            left xor right
        }

fun <T : CharSequence> Entity<T>.isEmpty(): Function<Boolean> =
        entityFunction(this) {
            it.isEmpty()
        }

fun <T : CharSequence> Entity<T>.isNotEmpty(): Function<Boolean> =
        entityFunction(this) {
            it.isNotEmpty()
        }

fun <T : CharSequence> Entity<T>.isNotBlank(): Function<Boolean> =
        entityFunction(this) {
            it.isNotBlank()
        }

fun <T> allOf(
        vararg entity: Entity<out T>,
        transformation: (Entity<out T>) -> Entity<Boolean>
): Function<Boolean> {
    val sources = entity.map {
        transformation.invoke(it)
    }.toTypedArray<Entity<*>>()
    return entityArrayFunction(sources) { args ->
        args.all { it as Boolean }
    }
}

fun <T> allOfValues(
        vararg entity: Entity<out T>,
        transformation: (T) -> Boolean
): Function<Boolean> {
    val sources = entity.map { it as Entity<*> }.toTypedArray()
    return entityArrayFunction(sources) { args ->
        args.all { transformation.invoke(it as T) }
    }
}

fun <T> anyOf(
        vararg entity: Entity<out T>,
        transformation: (Entity<out T>) -> Entity<Boolean>
): Function<Boolean> {
    val sources = entity.map {
        transformation.invoke(it)
    }.toTypedArray<Entity<*>>()
    return entityArrayFunction(sources) { args ->
        args.any { it as Boolean }
    }
}

fun <T> anyOfValues(
        vararg entity: Entity<out T>,
        transformation: (T) -> Boolean
): Function<Boolean> {
    val sources = entity.map { it as Entity<*> }.toTypedArray()
    return entityArrayFunction(sources) { args ->
        args.any { transformation.invoke(it as T) }
    }
}

fun <T> noneOf(
        vararg entity: Entity<out T>,
        transformation: (Entity<out T>) -> Entity<Boolean>
): Function<Boolean> {
    val sources = entity.map {
        transformation.invoke(it)
    }.toTypedArray<Entity<*>>()
    return entityArrayFunction(sources) { args ->
        args.none { it as Boolean }
    }
}

fun <T> noneOfValues(
        vararg entity: Entity<out T>,
        transformation: (T) -> Boolean
): Function<Boolean> {
    val sources = entity.map { it as Entity<*> }.toTypedArray()
    return entityArrayFunction(sources) { args ->
        args.none { transformation.invoke(it as T) }
    }
}
