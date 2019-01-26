package net.apptronic.common.android.ui

import net.apptronic.common.android.ui.viewmodel.entity.functions.Predicate
import net.apptronic.common.android.ui.viewmodel.entity.functions.variants.not
import kotlin.test.assertNull

fun assert(predicate: Predicate<Boolean>) {
    assert(predicate.getPredicateValue())
}

fun Predicate<Boolean>.assertTrue() {
    assert(getPredicateValue())
}

fun Predicate<Boolean>.assertFalse() {
    assert(not().getPredicateValue())
}

fun Predicate<Boolean>.assertNull() {
    assertNull(getPredicateValue())
}