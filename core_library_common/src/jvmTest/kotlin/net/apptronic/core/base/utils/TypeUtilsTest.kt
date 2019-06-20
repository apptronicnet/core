package net.apptronic.core.base.utils

import org.junit.Test

class TypeUtilsTest {

    @Test
    fun verifyIsSubClassOf() {
        assert(1 isInstanceOf Number::class)
        assert((1 isInstanceOf String::class).not())
    }

}