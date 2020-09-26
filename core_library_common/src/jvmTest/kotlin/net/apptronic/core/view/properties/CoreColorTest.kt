package net.apptronic.core.view.properties

import org.junit.Test
import kotlin.test.assertEquals

class CoreColorTest {

    @Test
    fun verifyRgbHex() {
        assertEquals(CoreColor.rgbHex(0xff0000), coreColor(255, 0, 0, 1))
        assertEquals(CoreColor.rgbHex(0x00ff00), coreColor(0, 255, 0, 1))
        assertEquals(CoreColor.rgbHex(0x0000ff), coreColor(0, 0, 255, 1))
        assertEquals(CoreColor.rgbHex(0xff0000, 0.5), coreColor(255, 0, 0, 0.5))
        assertEquals(CoreColor.rgbHex(0x00ff00, 0.5), coreColor(0, 255, 0, 0.5))
        assertEquals(CoreColor.rgbHex(0x0000ff, 0.5), coreColor(0, 0, 255, 0.5))
        assertEquals(CoreColor.rgbHex(0x800000), coreColor(128, 0, 0, 1))
        assertEquals(CoreColor.rgbHex(0x008000), coreColor(0, 128, 0, 1))
        assertEquals(CoreColor.rgbHex(0x000080), coreColor(0, 0, 128, 1))
        assertEquals(CoreColor.rgbHex(0x808000), coreColor(128, 128, 0, 1))
        assertEquals(CoreColor.rgbHex(0x008080), coreColor(0, 128, 128, 1))
        assertEquals(CoreColor.rgbHex(0x800080), coreColor(128, 0, 128, 1))
        assertEquals(CoreColor.rgbHex(0xff8000, 0.5), coreColor(255, 128, 0, 0.5))
        assertEquals(CoreColor.rgbHex(0x00ff80, 0.5), coreColor(0, 255, 128, 0.5))
        assertEquals(CoreColor.rgbHex(0x8000ff, 0.5), coreColor(128, 0, 255, 0.5))
    }

    @Test
    fun verifyArgbHex() {
        assertEquals(CoreColor.argbHex(0xffff0000), coreColor(255, 0, 0, 1))
        assertEquals(CoreColor.argbHex(0xff00ff00), coreColor(0, 255, 0, 1))
        assertEquals(CoreColor.argbHex(0xff0000ff), coreColor(0, 0, 255, 1))
        assertEquals(CoreColor.argbHex(0x80ff0000), coreColor(255, 0, 0, 0.5019608))
        assertEquals(CoreColor.argbHex(0x8000ff00), coreColor(0, 255, 0, 0.5019608))
        assertEquals(CoreColor.argbHex(0x800000ff), coreColor(0, 0, 255, 0.5019608))
        assertEquals(CoreColor.argbHex(0xff800000), coreColor(128, 0, 0, 1))
        assertEquals(CoreColor.argbHex(0xff008000), coreColor(0, 128, 0, 1))
        assertEquals(CoreColor.argbHex(0xff000080), coreColor(0, 0, 128, 1))
        assertEquals(CoreColor.argbHex(0xff808000), coreColor(128, 128, 0, 1))
        assertEquals(CoreColor.argbHex(0xff008080), coreColor(0, 128, 128, 1))
        assertEquals(CoreColor.argbHex(0xff800080), coreColor(128, 0, 128, 1))
        assertEquals(CoreColor.argbHex(0x80ff8000), coreColor(255, 128, 0, 0.5019608))
        assertEquals(CoreColor.argbHex(0x8000ff80), coreColor(0, 255, 128, 0.5019608))
        assertEquals(CoreColor.argbHex(0x808000ff), coreColor(128, 0, 255, 0.5019608))
    }

}