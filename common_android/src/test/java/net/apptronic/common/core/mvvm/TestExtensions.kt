package net.apptronic.common.core.mvvm

import net.apptronic.common.core.mvvm.viewmodel.entity.functions.Predicate
import net.apptronic.common.core.mvvm.viewmodel.entity.functions.variants.not
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