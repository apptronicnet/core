package net.apptronic.core.view

import net.apptronic.core.base.observable.Observable

fun CoreView.padding(
        all: Number? = null,
        left: Number? = null,
        right: Number? = null,
        top: Number? = null,
        bottom: Number? = null,
        start: Number? = null,
        end: Number? = null,
        horizontal: Number? = null,
        vertical: Number? = null,
) {
    all?.let { paddingTop(it); paddingBottom(it); paddingStart(it); paddingEnd(it) }
    left?.let { paddingLeft(it) }
    right?.let { paddingRight(it) }
    top?.let { paddingTop(it) }
    bottom?.let { paddingBottom(it) }
    start?.let { paddingStart(it) }
    end?.let { paddingEnd(it) }
    horizontal?.let { paddingStart(it); paddingEnd(it) }
    vertical?.let { paddingTop(it); paddingBottom(it) }
}

fun CoreView.paddingStart(value: Number) {
    paddingStart.set(value)
}

fun CoreView.paddingEnd(value: Number) {
    paddingEnd.set(value)
}

fun CoreView.paddingLeft(value: Number) {
    if (isLTR) {
        paddingStart.set(value)
    } else {
        paddingEnd.set(value)
    }
}

fun CoreView.paddingRight(value: Number) {
    if (isLTR) {
        paddingEnd.set(value)
    } else {
        paddingStart.set(value)
    }
}

fun CoreView.paddingTop(value: Number) {
    paddingTop.set(value)
}

fun CoreView.paddingBottom(value: Number) {
    paddingBottom.set(value)
}

fun CoreView.paddingStart(source: Observable<Number>) {
    paddingStart.set(source)
}

fun CoreView.paddingEnd(source: Observable<Number>) {
    paddingEnd.set(source)
}

fun CoreView.paddingLeft(source: Observable<Number>) {
    if (isLTR) {
        paddingStart.set(source)
    } else {
        paddingEnd.set(source)
    }
}

fun CoreView.paddingRight(source: Observable<Number>) {
    if (isLTR) {
        paddingEnd.set(source)
    } else {
        paddingStart.set(source)
    }
}

fun CoreView.paddingTop(source: Observable<Number>) {
    paddingTop.set(source)
}

fun CoreView.paddingBottom(source: Observable<Number>) {
    paddingBottom.set(source)
}