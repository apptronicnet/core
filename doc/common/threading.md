[Back to Manual](../manual.md)

Previous topic: [Creating Contexts and Components](components.md)

___

## Implementation of Components: threading(coroutines)

## Working with threads

**apptronic.net/core** framework designed to work with UI-first apps (especially mobile applications). Because of that all modern platforms based on "main thread" (Android, iOS, Java Swing) or single-thread by design (JavaScript) framework also uses single-thread architecture.

All behavior are initially work in one single thread, preventing any concurrency issues and performance impact caused by thread synchronization etc.

But not all things can be done synchronously in main thread, so, instead of classical threading **apptronic.net/core** supports asynchronous programming with Kotlin/Coroutines.

### Working with coroutines

[Kotlin Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) is modern library for implementation of asynchronous programming.

It is perfectly matches for purposes of mobile app development and available in Kotlin/Multiplatform. **apptronic.net/core** contains integration to work for Kotlin Coroutnies out of the box. Also it uses coroutines for some internals like async notification/updates for ViewModels.

First it needed to add coroutines as a dependency:
```groovy
implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.3.5"
```
Do not forget to add platform-specific dependency for each platform to provide ```Dispatchers.Main``` for framework execution.

For testing purposes it possible to create testing **Context** with another dispatcher(for example ```Dispatchers.Unconfined```) allowing to execute synchronous tests.

Generally coroutines can be used in same way as it used in other platforms, but **apptronic.net/core** contains several integrations to make it easier.

##### Coroutine launcher

```kotlin
interface CoroutineLauncher {

    val context: Context

    val coroutineScope: CoroutineScope

    fun launch(
            coroutineContext: CoroutineContext = context.defaultDispatcher,
            start: CoroutineStart = CoroutineStart.DEFAULT,
            block: suspend CoroutineScope.() -> Unit
    )

}
```

This class is very similar to ```CoroutineScope``` because of it have ```fun launch()``` with same parameters.

Main difference is that it automatically handles ```CoroutineLauncherCancellationException``` (subclass of ```CancellationException```) which thrown when ```CoroutineLauncher``` cancels its ```CoroutineContext```.

Main reason to use ```CoroutineLauncher``` instead of creating ```CoroutineScope``` is that **apptronic.net/core** can provide ```CoroutineLauncher``` based on **Context** and active **LifecycleStage** when cancellation of ```CoroutineContext``` is bound to **Context** termination or exiting from specific **LifecycleStage**.

This is used to reactive programming module of **apptronic.net/core** framework, but also can be used to launch coroutines by developer.

##### Scoped CoroutineLauncher
```kotlin
fun Context.coroutineLauncherScoped(): CoroutineLauncher
```
Returns ```CoroutineLauncher```, which bound to active **LifecycleStage** of current **Context** and automatically cancels its ```CoroutineContext``` when **LifecycleStage** is exited.
##### Local CoroutineLauncher
```kotlin
fun Context.coroutineLauncherLocal(): CoroutineLauncher
```
Returns ```CoroutineLauncher```, which bound to root **LifecycleStage** of current **Context**. It cancels its ```CoroutineContext``` when **Context*** is terminated.
##### Global CoroutineLauncher
```kotlin
fun Context.coroutineLauncherGlobal(): CoroutineLauncher
```
Returns ```CoroutineLauncher```, which bound to root **LifecycleStage** of root **Context** of whole **Context** hierarchy (**Core Context**). It cancels its ```CoroutineContext``` when **Core Context*** is terminated (which may never happen). If **Core Context** is never terminated its ```CoroutineScope``` works like ```GlobalScope```.
##### Preventing unexpected issues while using CoroutineLaunchers
As ```CoroutineContext``` of concrete ```CoroutineLauncher``` cancelled automatically based on type of ```CoroutineLauncher``` it needed to remember when it needed to use each of them.

If async action is getting some data to use inside of **Context** it good idea to use ```coroutineLauncherLocal()```:
- when **Context** will be terminated all coroutines will be cancelled as not needed, releasing CPU resources.
- it not depends on current active **LifecycleStage** in context so can be called at any moment.

If async action actual only while **Lifecycle** is inside some **LifecycleStage** (for example, until **ViewModel** is bound to View) it is time to use ```coroutineLauncherScoped()```.

If async action should be independent for current **Context** (for example, sending entered data to backend) it can be interrupted by terminating **Context** (for example, when screen closed). In that case it is needed to use ```coroutineLauncherGlobal()```.

When something requires usage of ```coroutineLauncherGlobal()``` it's time to think about moving that action to separate **Component** while works inside of **Code Context** or other top-level **Context** and use Dependency Injection to access to it.

##### Accessing  ```CoroutineLauncher``` from Component

Instead of invoking local ```context``` from **Component** each tme when it needed to create ```CoroutineLauncher``` there is extension for accessing ```CoroutineLauncher``` from ```Component``` class:
```kotlin
fun Component.coroutineLaunchers(): CoroutineLaunchers

interface CoroutineLaunchers {
    val global: CoroutineLauncher
    val local: CoroutineLauncher
    val scoped: CoroutineLauncher
}
``` 
```coroutineLaunchers().global``` is same as ```Context.coroutineLauncherGlobal```

```coroutineLaunchers().local``` is same as ```Context.coroutineLauncherLocal```

```coroutineLaunchers().scoped``` is same as ```Context.coroutineLauncherScoped```

##### Working directly with CoroutineScope of CoroutineLauncher

It is possible to use ```CoroutineScope.coroutineScope``` to launch coroutines directly from ```CoroutineScope```. Note, that all these coroutines can be cancelled as ```CoroutineScope``` in ```CoroutineLauncher``` cancelled automatically based on concrete type of```CoroutineLauncher```.

Manual cancellation of ```CoroutineScope``` from ```CoroutineLauncher``` will cancel all coroutines launched by ```CoroutineLauncher``` which may cause unexpected behavior and not recommended.

Note, that each invocation will create a new instance of ```CoroutinaLauncher```. If some async action can be called many times it better to store instance ```CoroutinaLauncher``` and reuse it.

##### Throttling and debouncing action inside CoroutineLauncher

In some cases it needed to guarantee that actions executed one after another, preventing parallel executions.

In that case is possible to call ```CoroutineLauncher.debouncer()``` which returns new instance of ```CoroutineLauncher```, which uses same ```CoroutineScope```, but handles invocation queue inside and launches next coroutine only after the previous coroutine completed its execution.

To limit size of queue it needed to call ```CoroutineLauncher.debouncer(size: Int)``` instead. It will do same as ```CoroutineLauncher.debouncer()``` but with limited size of execution queue. In case of queue overflow the oldest not executed coroutine will be thrown out of queue.

___

[Back to Manual](../manual.md)

Next topic: [Implementation of Components: reactive behavior](reactive_behavior.md)
