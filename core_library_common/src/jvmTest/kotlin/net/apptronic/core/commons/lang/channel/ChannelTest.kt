package net.apptronic.core.commons.lang.channel

import net.apptronic.core.BaseContextTest
import net.apptronic.core.entity.commons.value
import net.apptronic.core.record
import org.junit.Test

class ChannelTest : BaseContextTest() {

    val source = value<String>()
    val channel = source.asChannel()
    val result = channel.asEntity(context)
    val data = result.record()

    @Test
    fun notWrittenTest() {
        source.set("1")
        data.assertItems("1")

        source.set("2")
        data.assertItems("1", "2")

        source.set("35")
        data.assertItems("1", "2", "35")
    }

}