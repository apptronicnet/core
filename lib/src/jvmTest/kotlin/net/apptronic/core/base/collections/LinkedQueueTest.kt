package net.apptronic.core.base.collections

import org.junit.Test

class LinkedQueueTest {

    @Test
    fun runTest() {
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

}