[Back to Manual](../manual.md)

Previous topic: [Creating Contexts and Components](components.md)
___
## Implementation of Components, threading(coroutines) and reactive behavior

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

TBD: when to use coroutineLauncherScoped

TBD: when to use coroutineLauncherGlobal

TBD: reactive programming with Entities, Computable properties and using with Coroutines