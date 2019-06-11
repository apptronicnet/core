package net.apptronic.test.commons_sample_app.lazylist

import net.apptronic.core.component.task.SchedulerMode
import net.apptronic.core.component.task.executeWhen
import net.apptronic.core.component.task.genericTaskScheduler
import net.apptronic.core.component.task.taskScheduler
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.threading.WorkerDefinition
import kotlin.random.Random

private val CHARS = "qwertyuiopasdfghjklzxcvbnm1234567890"

private fun randomString(): String {
    return String((0..16).map {
        CHARS[Random.nextInt(CHARS.length)]
    }.toCharArray())
}

class LazyListViewModel(context: ViewModelContext) : ViewModel(context), LazyListItemClickListener {

    init {
        getProvider().addInstance(LazyListItemClickListenerDescriptor, this)
    }

    val items = listRecyclerNavigator(LazyListBuilder())

    private val loaderTask = genericTaskScheduler(SchedulerMode.Debounce) {
        onStart(WorkerDefinition.BACKGROUND_PARALLEL_SHARED).map {
            (1..100).map {
                LazyListItem(it.toLong(), it, randomString())
            }
        }.sendResultTo(items)
    }.executeWhen(whenCreated())

    private val updateTask = taskScheduler<Long> {
        onStart(WorkerDefinition.DEFAULT).map {
            items.get() to it
        }.switchWorker(WorkerDefinition.BACKGROUND_PARALLEL_SHARED).map { paur ->
            paur.first.map {
                if (it.id == paur.second) {
                    LazyListItem(
                        it.id,
                        it.number,
                        randomString()
                    )
                } else {
                    it
                }
            }
        }.sendResultTo(items)
    }

    override fun onClick(id: Long) {
        updateTask.execute(id)
    }

}