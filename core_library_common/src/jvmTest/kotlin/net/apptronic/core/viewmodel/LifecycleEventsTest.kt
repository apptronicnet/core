package net.apptronic.core.viewmodel

import net.apptronic.core.context.terminate
import net.apptronic.core.record
import kotlin.test.Test

class LifecycleEventsTest : TestViewModel() {

    private val isAttachedRecord = whenAttached().record()
    private val isBoundRecord = whenBound().record()
    private val isVisibleRecord = whenVisible().record()
    private val isFocusedRecord = whenFocused().record()

    @Test
    fun shouldRecordAll() {
        isAttachedRecord.assertSize(0)
        isBoundRecord.assertSize(0)
        isVisibleRecord.assertSize(0)
        isFocusedRecord.assertSize(0)
        attach()
        isAttachedRecord.assertSize(1)
        bind()
        show()
        focus()
        isBoundRecord.assertSize(1)
        isVisibleRecord.assertSize(1)
        isFocusedRecord.assertSize(1)
        unfocus()
        hide()
        unbind()
        detach()
        isAttachedRecord.assertSize(1)
        isBoundRecord.assertSize(1)
        isVisibleRecord.assertSize(1)
        isFocusedRecord.assertSize(1)
        attach()
        bind()
        show()
        focus()
        isAttachedRecord.assertSize(2)
        isBoundRecord.assertSize(2)
        isVisibleRecord.assertSize(2)
        isFocusedRecord.assertSize(2)
        unfocus()
        hide()
        unbind()
        detach()
        isAttachedRecord.assertSubscribed()
        isBoundRecord.assertSubscribed()
        isVisibleRecord.assertSubscribed()
        isFocusedRecord.assertSubscribed()
        terminate()
        isAttachedRecord.assertUnsubscribed()
        isBoundRecord.assertUnsubscribed()
        isVisibleRecord.assertUnsubscribed()
        isFocusedRecord.assertUnsubscribed()
    }

}