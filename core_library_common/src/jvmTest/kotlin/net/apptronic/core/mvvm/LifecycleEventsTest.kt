package net.apptronic.core.mvvm

import net.apptronic.core.component.terminate
import net.apptronic.core.record
import kotlin.test.Test

class LifecycleEventsTest : TestViewModel() {

    private val isCreatedRecord = whenCreated().record()
    private val isBoundRecord = whenCreated().record()
    private val isVisibleRecord = whenCreated().record()
    private val isFocusedRecord = whenCreated().record()

    @Test
    fun shouldRecordAll() {
        create()
        isCreatedRecord.assertSize(1)
        bind()
        show()
        focus()
        isBoundRecord.assertSize(1)
        isVisibleRecord.assertSize(1)
        isFocusedRecord.assertSize(1)
        unfocus()
        hide()
        unbind()
        destroy()
        isCreatedRecord.assertSize(1)
        isBoundRecord.assertSize(1)
        isVisibleRecord.assertSize(1)
        isFocusedRecord.assertSize(1)
        create()
        bind()
        show()
        focus()
        isCreatedRecord.assertSize(2)
        isBoundRecord.assertSize(2)
        isVisibleRecord.assertSize(2)
        isFocusedRecord.assertSize(2)
        unfocus()
        hide()
        unbind()
        destroy()
        isCreatedRecord.assertSubscribed()
        isBoundRecord.assertSubscribed()
        isVisibleRecord.assertSubscribed()
        isFocusedRecord.assertSubscribed()
        terminate()
        isCreatedRecord.assertUnsubscribed()
        isBoundRecord.assertUnsubscribed()
        isVisibleRecord.assertUnsubscribed()
        isFocusedRecord.assertUnsubscribed()
    }

}