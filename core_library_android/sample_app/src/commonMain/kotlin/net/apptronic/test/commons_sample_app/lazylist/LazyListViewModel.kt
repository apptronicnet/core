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
            mutableListOf<Any>().apply {
                add(StaticItem("start", "Start", randomString()))
                addAll(
                    (1..100).map {
                        LazyListItem(it.toLong(), it, randomString())
                    }
                )
                add(StaticItem("end", "End", randomString()))
            }
        }.sendResultTo(items)
    }.executeWhen(whenCreated())

    private val updateTask = taskScheduler<Long> {
        onStart(WorkerDefinition.DEFAULT).map {
            items.get() to it
        }.switchWorker(WorkerDefinition.BACKGROUND_PARALLEL_SHARED).map { pair ->
            pair.first.map { item ->
                (item as? LazyListItem)?.let {
                    if (it.id == pair.second) {
                        LazyListItem(
                            it.id,
                            it.number,
                            randomString()
                        )
                    } else {
                        it
                    }
                } ?: (item as StaticItem).also {
                    it.text = randomString()
                }
            }
        }.sendResultTo(items)
    }

    override fun onClick(id: Long) {
        updateTask.execute(id)
    }

}