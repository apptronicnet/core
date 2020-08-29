package net.apptronic.core.base.collections

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Test

class LinkedQueueTest {

    @Test
    fun verifyTake() {
        val queue = LinkedQueue<Int>()
        assert(queue.take() == null)
        queue.add(1)
        assert(queue.take() == 1)
        assert(queue.take() == null)
        queue.add(2)
        queue.add(3)
        assert(queue.take() == 2)
        assert(queue.take() == 3)
        assert(queue.take() == null)
        queue.add(4)
        queue.add(5)
        queue.add(6)
        assert(queue.take() == 4)
        assert(queue.take() == 5)
        assert(queue.take() == 6)
        assert(queue.take() == null)
        assert(queue.take() == null)
        assert(queue.take() == null)
        queue.add(7)
        assert(queue.take() == 7)
        assert(queue.take() == null)
        assert(queue.take() == null)
        queue.add(8)
        queue.add(9)
        queue.add(10)
        queue.add(11)
        queue.add(12)
        assert(queue.take() == 8)
        assert(queue.take() == 9)
        assert(queue.take() == 10)
        assert(queue.take() == 11)
        assert(queue.take() == 12)
        assert(queue.take() == null)
        assert(queue.take() == null)
        assert(queue.take() == null)
        assert(queue.take() == null)
    }

    @Test
    fun verifyTakeAwait() {
        val queue = LinkedQueue<Int>()
        runBlocking {
            withTimeout(1000) {
                assert(queue.take() == null)
                queue.add(1)
                assert(queue.takeAwait() == 1)
                queue.add(2)
                queue.add(3)
                queue.add(4)
                assert(queue.takeAwait() == 2)
                assert(queue.takeAwait() == 3)
                assert(queue.takeAwait() == 4)
                launch {
                    queue.add(5)
                }
                assert(queue.take() == null)
                assert(queue.takeAwait() == 5)
            }
        }
    }

}