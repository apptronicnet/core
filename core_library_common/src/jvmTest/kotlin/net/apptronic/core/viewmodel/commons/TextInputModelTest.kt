package net.apptronic.core.viewmodel.commons

import net.apptronic.core.BaseContextTest
import net.apptronic.core.record
import org.junit.Test
import kotlin.test.assertEquals

class TextInputModelTest : BaseContextTest() {

    private var content: String = ""
        set(value) {
            field = value
            afterTextChanged(value)
        }
    private var selection = 0..0

    private fun inputChars(chars: String) {
        content = content.substring(0, selection.first) + chars + content.substring(selection.last)
        selection = content.length..content.length
    }

    private fun backspace() {
        when {
            selection.first == selection.last && selection.first > 0 -> {
                content += content.substring(0, selection.first - 1) + content.substring(selection.last)
            }
            else -> {
                content += content.substring(0, selection.first) + content.substring(selection.last)
            }
        }
    }

    val textInput = textInput("")

    private fun afterTextChanged(text: String) {
        if (textInput.get() != text) {
            textInput.update(text)
            textInput.selection.update(selection)
        }
    }

    init {
        textInput.subscribe {
            if (content != it) {
                content = it
            }
        }
        textInput.selection.subscribe {
            if (selection != it) {
                selection = it
            }
        }
    }

    private fun assertContent(content: String, selection: IntRange) {
        assertEquals(content, this.content)
        assertEquals(selection, this.selection)
    }

    private val textChanged = textInput.record()
    private val textUpdates = textInput.updates.record()

    init {
        textChanged.clear()
        textUpdates.clear()
    }

    private fun assertChanges(vararg values: String) {
        textChanged.assertItems(*values)
        textChanged.clear()
    }

    private fun assertUpdates(vararg values: String) {
        textUpdates.assertItems(*values)
        textUpdates.clear()
    }

    @Test
    fun verifyTestSetup() {
        inputChars("1")
        assertContent("1", 1..1)
        assertChanges("1")
        assertUpdates("1")

        inputChars("2")
        assertContent("12", 2..2)
        assertChanges("12")
        assertUpdates("12")

        textInput.set("345")
        assertContent("345", 3..3)
        assertChanges("345")
        assertUpdates()

        textInput.setTextOnly("TestText")
        assertContent("TestText", 3..3)
        assertChanges("TestText")
        assertUpdates()

        textInput.setText("AnotherText")
        assertContent("AnotherText", 11..11)
        assertChanges("AnotherText")
        assertUpdates()

        textInput.setText("SuperChangedText", 10)
        assertContent("SuperChangedText", 10..10)
        assertChanges("SuperChangedText")
        assertUpdates()

        textInput.setText("Some more and more", 5..11)
        assertContent("Some more and more", 5..11)
        assertChanges("Some more and more")
        assertUpdates()

        textInput.setSelection(3)
        assertContent("Some more and more", 3..3)
        assertChanges()
        assertUpdates()

        textInput.setSelection(4..7)
        assertContent("Some more and more", 4..7)
        assertChanges()
        assertUpdates()
    }

    @Test
    fun verifySelectionCannotBeSetOutOfRangeEmpty() {
        textInput.setSelection(-3)
        assertContent("", 0..0)

        textInput.setSelection(5)
        assertContent("", 0..0)

        textInput.setSelection(-3..-5)
        assertContent("", 0..0)

        textInput.setSelection(-3..8)
        assertContent("", 0..0)

        textInput.setSelection(2..6)
        assertContent("", 0..0)
    }

    @Test
    fun verifySelectionCannotBeSetOutOfRangeFilled() {
        textInput.setText("10 chars..", 10)

        textInput.setSelection(-3)
        assertContent("10 chars..", 0..0)

        textInput.setSelection(15)
        assertContent("10 chars..", 10..10)

        textInput.setSelection(-3..-5)
        assertContent("10 chars..", 0..0)

        textInput.setSelection(-3..5)
        assertContent("10 chars..", 0..5)

        textInput.setSelection(-5..16)
        assertContent("10 chars..", 0..10)

        textInput.setSelection(5..16)
        assertContent("10 chars..", 5..10)

        textInput.setSelection(12..16)
        assertContent("10 chars..", 10..10)
    }

    @Test
    fun verifySendsUpdatesOnly() {
        inputChars("2")
        assertContent("2", 1..1)
        assertChanges("2")
        assertUpdates("2")

        inputChars("romeo")
        assertContent("2romeo", 6..6)
        assertChanges("2romeo")
        assertUpdates("2romeo")

        // when changing to same text updates should be not sent
        selection = 0..6
        inputChars("2romeo")
        assertContent("2romeo", 6..6)
        assertChanges()
        assertUpdates()

        textInput.setText("2romeo")
        assertContent("2romeo", 6..6)
        assertChanges()
        assertUpdates()

        textInput.setTextOnly("2romeo")
        assertContent("2romeo", 6..6)
        assertChanges()
        assertUpdates()

        textInput.setText("2romeo", 2)
        assertContent("2romeo", 2..2)
        assertChanges()
        assertUpdates()

        textInput.setText("2romeo", 2..4)
        assertContent("2romeo", 2..4)
        assertChanges()
        assertUpdates()
    }

    @Test
    fun changeSelectionShouldNotMakeChanges() {
        textInput.set("SomeText")
        assertChanges("SomeText")
        assertUpdates()

        assertContent("SomeText", 8..8)
        assertChanges()
        assertUpdates()

        textInput.setSelection(3)
        assertContent("SomeText", 3..3)
        assertChanges()
        assertUpdates()

        textInput.setSelection(3..7)
        assertContent("SomeText", 3..7)
        assertChanges()
        assertUpdates()
    }

}