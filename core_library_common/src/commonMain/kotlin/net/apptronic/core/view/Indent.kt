package net.apptronic.core.view

import net.apptronic.core.base.observable.Observable

fun CoreView.indent(
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
    all?.let { indentTop(it); indentBottom(it); indentStart(it); indentEnd(it) }
    left?.let { indentLeft(it) }
    right?.let { indentRight(it) }
    top?.let { indentTop(it) }
    bottom?.let { indentBottom(it) }
    start?.let { indentStart(it) }
    end?.let { indentEnd(it) }
    horizontal?.let { indentStart(it); indentEnd(it) }
    vertical?.let { indentTop(it); indentBottom(it) }
}

fun CoreView.indentStart(value: Number) {
    indentStart.set(value)
}

fun CoreView.indentEnd(value: Number) {
    indentEnd.set(value)
}

fun CoreView.indentLeft(value: Number) {
    if (isLTR) {
        indentStart.set(value)
    } else {
        indentEnd.set(value)
    }
}

fun CoreView.indentRight(value: Number) {
    if (isLTR) {
        indentEnd.set(value)
    } else {
        indentStart.set(value)
    }
}

fun CoreView.indentTop(value: Number) {
    indentTop.set(value)
}

fun CoreView.indentBottom(value: Number) {
    indentBottom.set(value)
}

fun CoreView.indentStart(source: Observable<Number>) {
    indentStart.set(source)
}

fun CoreView.indentEnd(source: Observable<Number>) {
    indentEnd.set(source)
}

fun CoreView.indentLeft(source: Observable<Number>) {
    if (isLTR) {
        indentStart.set(source)
    } else {
        indentEnd.set(source)
    }
}

fun CoreView.indentRight(source: Observable<Number>) {
    if (isLTR) {
        indentEnd.set(source)
    } else {
        indentStart.set(source)
    }
}

fun CoreView.indentTop(source: Observable<Number>) {
    indentTop.set(source)
}

fun CoreView.indentBottom(source: Observable<Number>) {
    indentBottom.set(source)
}