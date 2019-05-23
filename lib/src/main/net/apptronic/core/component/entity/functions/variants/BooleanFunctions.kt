package net.apptronic.core.component.entity.functions.variants

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.functions.entityArrayFunction
import net.apptronic.core.component.entity.functions.entityFunction

fun <T> Entity<T>.isNull(): Entity<Boolean> =
    entityFunction(this) {
        it == null
    }

fun <T> Entity<T>.isNotNull(): Entity<Boolean> =
    entityFunction(this) {
        it != null
    }

fun <T> Entity<T>.anyValue(): Entity<Boolean> =
    entityFunction(this) {
        true
    }

fun Entity<Boolean>.isTrue(): Entity<Boolean> =
    entityFunction(this) {
        it
    }

fun Entity<Boolean>.isFalse() =
    entityFunction(this) {
        it.not()
    }

fun Entity<Boolean>.not(): Entity<Boolean> =
    entityFunction(this) {
        it.not()
    }

fun Entity<Boolean?>.isTrueOrNull() =
    entityFunction(this) {
        it == null || it
    }

fun Entity<Boolean?>.isFalseOrNull() =
    entityFunction(this) {
        it == null || it.not()
    }

infix fun <A, B> Entity<A>.isEqualsTo(another: Entity<B>) =
    entityFunction(this, another) { left, right ->
        left == right
    }

infix fun <A, B> Entity<A>.isEqualsTo(another: B) =
    entityFunction(this) {
        it == another
    }

infix fun <A, B> Entity<A>.isNotEqualsTo(another: Entity<B>) =
    entityFunction(this, another) { left, right ->
        left != right
    }

infix fun <A, B> Entity<A>.isNotEqualsTo(another: B) =
    entityFunction(this) {
        it != another
    }

infix fun Entity<Boolean>.and(another: Entity<Boolean>) =
    entityFunction(this, another) { left, right ->
        left and right
    }

infix fun Entity<Boolean>.or(another: Entity<Boolean>) =
    entityFunction(this, another) { left, right ->
        left or right
    }

infix fun Entity<Boolean>.xor(another: Entity<Boolean>) =
    entityFunction(this, another) { left, right ->
        left xor right
    }

fun <T : CharSequence> Entity<T>.isEmpty() =
    entityFunction(this) {
        it.isEmpty()
    }

fun <T : CharSequence> Entity<T>.isNotEmpty() =
    entityFunction(this) {
        it.isNotEmpty()
    }

fun <T : CharSequence> Entity<T>.isNotBlank() =
    entityFunction(this) {
        it.isNotBlank()
    }

fun <T> allOf(
    vararg entity: Entity<out T>,
    transformation: (Entity<out T>) -> Entity<Boolean>
): Entity<Boolean> {
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
): Entity<Boolean> {
    val sources = entity.map { it as Entity<*> }.toTypedArray()
    return entityArrayFunction(sources) { args ->
        args.all { transformation.invoke(it as T) }
    }
}

fun <T> anyOf(
    vararg entity: Entity<out T>,
    transformation: (Entity<out T>) -> Entity<Boolean>
): Entity<Boolean> {
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
): Entity<Boolean> {
    val sources = entity.map { it as Entity<*> }.toTypedArray()
    return entityArrayFunction(sources) { args ->
        args.any { transformation.invoke(it as T) }
    }
}

fun <T> noneOf(
    vararg entity: Entity<out T>,
    transformation: (Entity<out T>) -> Entity<Boolean>
): Entity<Boolean> {
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
): Entity<Boolean> {
    val sources = entity.map { it as Entity<*> }.toTypedArray()
    return entityArrayFunction(sources) { args ->
        args.none { transformation.invoke(it as T) }
    }
}
