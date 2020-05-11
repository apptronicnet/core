package net.apptronic.test.commons_sample_app.lazylist

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.coroutines.coroutineLauncherContextual
import net.apptronic.core.component.coroutines.debouncer
import net.apptronic.core.mvvm.viewmodel.EMPTY_VIEW_MODEL_CONTEXT
import net.apptronic.core.mvvm.viewmodel.ViewModel
import kotlin.random.Random

private const val CHARS = "qwertyuiopasdfghjklzxcvbnm1234567890"

private fun randomString(): String {
    return String((0..16).map {
        CHARS[Random.nextInt(CHARS.length)]
    }.toCharArray())
}

class LazyListViewModel(parent: Context) : ViewModel(parent, EMPTY_VIEW_MODEL_CONTEXT),
    LazyListItemClickListener {

    private val debouncer = context.coroutineLauncherContextual().debouncer()

    val items = listRecyclerNavigator(LazyListBuilder())

    init {
        context.dependencyDispatcher().addInstance(LazyListItemClickListenerDescriptor, this)
        context.coroutineLauncherContextual().launch {
            val result = withContext(Dispatchers.Default) {
                mutableListOf<Any>().apply {
                    add(StaticItem("start", "Start", randomString()))
                    addAll(
                        (1..100).map {
                            LazyListItem(it.toLong(), it, randomString())
                        }
                    )
                    add(StaticItem("end", "End", randomString()))
                }
            }
            items.set(result)
        }
    }

    override fun onClick(itemId: Long) {
        debouncer.launch {
            val current = items.get()
            val next = withContext(Dispatchers.Default) {
                current.map { item ->
                    (item as? LazyListItem)?.let {
                        if (it.id == itemId) {
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
            }
            items.set(next)
        }
    }

}