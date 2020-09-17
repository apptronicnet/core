[Back to Manual](../manual.md)

Previous topic: [Implementation of Components: reactive behavior](reactive_behavior.md)
___

## Implementation of Components: advanced reactive behavior

### Exception handling

In comparison to RxJava **Entity** is an infinite stream (until **LifecycleStage** is not exited) and is not designed to operate with errors.

So throwing exception inside of any reactive function will cause crash.

So error handling philosophy in **apptronic.net/core** framework is completely different.

The main rule: Exception is not a kind or norm, so it cannot be ignored, it should be explicitly handled in place where it can happen.

```kotlin
class SomeViewModel(context: ViewModelContext) : BaseComponent(context) {

    val repository = inject<NetworkRepository>()

    val id = value<String>()

    val data = it.mapSuspend {
        repository.loadFromNetwork(it)
    }

}
```

There is no Exception handler, so in case if ```repository.loadFromNetwork(it)``` will throw any Exception app will crash.

```kotlin
class SomeViewModel(context: ViewModelContext) : BaseComponent(context) {

    val repository = inject<NetworkRepository>()

    val id = value<String>()
    
    private val retryEvent = genericEvent()

    val data = it.resendWhen(retryEvent).mapSuspend {
        tryCatchingSuspend {
            repository.loadFromNetwork(it)
        }
    }.onException {
        showErrorDialog(it)
    }

    fun showErrorDialog(e: Exception) {
        // invoke showing error dialog
    }

    fun retry() {
        retryEvent.sendEvent()
    }

    fun cancel() {
        // close [SomeViewModel]
    }

}
```

- Exception thrown by ```repository.loadFromNetwork(it)``` will be handled inside of ```onException``` block. 
- ```data``` will be updated only when ```repository.loadFromNetwork(it)``` returns some result
- when ```retry()``` invoked - current value of ```id``` will be resent and map function called again.

### Passing Exception through stream

In case if Exception should be passed through stream then it should be supported by stream.

Methods ```tryCatching``` and ```tryCatchingSuspend``` returns ```TryCatchResult``` object, which can have types of ```Success``` or ```Failure```.

Then ```TryCatchResult``` can be used as return type of mapping or other function which will result in creating ```Entity<TryCatchResult<T>>``` where ```T``` is success type.

### Waiting for Entity condition

In some cases it needed to perform action only on some condition. For example:

```kotlin
class MyComponent(context: Context) : BaseComponent(context) {

    val repository = inject<SomeRepository>()

    val data = repository.observeData(context)

    // this creates new Property<Boolean> with default value [false]
    // but automatically sets to [true] when [data] emits any item
    val isDataLoaded = data.whenAnyValue()

    fun runWhenDataLoaded() {
        contextCoroutineScope.launch {
            isDataLoaded.awaitUntilValue(true)
            // here [data] already set, can perform actions with data
        }
    }

}
```

Sample above demonstrates implementation of code which is awaits for condition of some ```Entity```.

This can be used, for example, for showing up next screen only when data of that screen loaded.

```kotlin

fun Contextual.detailsViewModel(id: String) = DetailsViewModel(viewModelContext(), id)

class DetailsViewModel(context: ViewModelContext, id: String) : ViewModel(context) {

    val repository = inject<SomeRepository>()
    val data = value<DetailedData>()
    val isDataLoaded = data.whenAnyValue()

    init {
        contextCoroutineScope.launch {
            data.set(repository.loadDetailedData(id))
        }
    }

}

class SomeParentViewModel(context: ViewModelContext) : ViewModel(context) {

    val navigator = stackNavigator()
    
    val showOverlayProgress = value(false)

    private var openDetailsJob: Job? = null

    fun cancelProgress() {
        openDetailsJob?.cancel()
        openDetailsJob = null
    }

    fun openDetails(id: String) {
        openDetailsJob = contextCoroutineScope.launch {
            showOverlayProgress.set(true)
            // create new instance of DetailsViewModel inside of [navigator] context
            val detailsViewModel = navigator.context.detailsViewModel()
            try {
                detailsViewModel.isDataLoaded.awaitUntilTrue()
                navigator.add(detailsViewModel, BasicTransition.Forward)
            } catch (e: CancellationException) {
                // in case of cancellation [detailsViewModel] will never be added to [navigator]
                // in that case need to terminate [detailsViewModel] manually 
                // as [navigator] manages lifecycle only for [ViewModel]s which is added to it
                detailsViewModel.terminate()
                throw e
            } finally {
                showOverlayProgress.set(false)
            }   
        }
    }   

}
```

In this sample shown ability to load data inside ```DetailsViewModel``` but not show it until its data loaded.

Overlay progress shown on top of parent screen and ```DetailsViewModel``` displayed with all data loaded.

An additional feature is that loading can be cancelled. In that case next screen will not be shown, unused ```DetailsViewModel``` terminated as described in comments.

___

[Back to Manual](../manual.md)

Next topic: [App UI implementation with ViewModels](view_models.md)