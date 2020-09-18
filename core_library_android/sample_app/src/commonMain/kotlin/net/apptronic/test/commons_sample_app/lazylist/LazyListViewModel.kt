package net.apptronic.test.commons_sample_app.lazylist

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.context.viewModelContext
import net.apptronic.core.component.coroutines.contextCoroutineScope
import net.apptronic.core.component.coroutines.serialThrottler
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.mvvm.viewmodel.navigation.listDynamicNavigator
import kotlin.random.Random

private const val CHARS = "qwertyuiopasdfghjklzxcvbnm1234567890"

private fun randomString(): String {
    return (0..16).map {
        CHARS[Random.nextInt(CHARS.length)]
    }.toCharArray().concatToString()
}

fun Contextual.lazyListViewModel() = LazyListViewModel(viewModelContext())

class LazyListViewModel internal constructor(context: ViewModelContext) : ViewModel(context),
    LazyListItemClickListener {

    private val throttler = contextCoroutineScope.serialThrottler()

    val items = listDynamicNavigator(LazyListBuilder())

    init {
        context.dependencyDispatcher.addInstance(LazyListItemClickListenerDescriptor, this)
        contextCoroutineScope.launch {
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

    override fun onClick(id: Long) {
        throttler.launch {
            val current = items.get()
            val next = withContext(Dispatchers.Default) {
                current.map { item ->
                    (item as? LazyListItem)?.let {
                        if (it.id == id) {
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