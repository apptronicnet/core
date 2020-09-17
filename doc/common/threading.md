[Back to Manual](../manual.md)

Previous topic: [Creating Contexts and Components](components.md)

___

## Implementation of Components: threading (coroutines)

## Working with threads

**apptronic.net/core** framework designed to work with UI-first apps (especially mobile applications). Because of that all modern platforms based on "main thread" (Android, iOS, Java Swing) or single-thread by design (JavaScript) framework also uses single-thread architecture.

All behavior are initially work in one single thread, preventing any concurrency issues and performance impact caused by thread synchronization etc.

But not all things can be done synchronously in main thread, so, instead of classical threading **apptronic.net/core** supports asynchronous programming with Kotlin/Coroutines.

### Working with coroutines

[Kotlin Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) is modern library for implementation of asynchronous programming.

It is perfectly matches for purposes of mobile app development and available in Kotlin/Multiplatform. **apptronic.net/core** contains integration to work for Kotlin Coroutnies out of the box. Also it uses coroutines for some internals like async notification/updates for ViewModels.

First it needed to add coroutines as a dependency:
```groovy
implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.3.9"
```
Do not forget to add platform-specific dependency for each platform to provide ```Dispatchers.Main``` for framework execution.

For testing purposes it possible to create testing **Context** with another dispatcher(for example ```Dispatchers.Unconfined```) allowing to execute synchronous tests.

Generally coroutines can be used in same way as it used in other platforms, but **apptronic.net/core** contains several integrations to make it easier.

##### Context-bound coroutine scopes

Framework provides coroutine scopes bound for each **Context** instance.

It can be accessed from any **Component**:

```kotlin
class SomeComponent(context: Context) : BaseComponent(context) {

    fun runGlobal() {
        globalCoroutineScope.launch {
            // coroutine execution core
        }   
    }

    fun runLocal() {
        contextCoroutineScope.launch {
            // coroutine execution core
        }   
    }

    fun runOnCurrentStage() {
        lifecycleCoroutineScope.launch {
            // coroutine execution core
        }   
    }

}
```

```Contextual.globalCoroutineScope``` is an instance of ```CoroutineScope``` which is bound to core **Context** of **Context** hierarchy. It is very similar to ```GlobalScope```, but defined and exists in core **Context**. In case if that core **Context** terminated - ```Contextual.globalCoroutineScope``` will be cancelled. This may be useful for testing purposes. 

```Contextual.contextCoroutineScope``` is an instance of ```CoroutineScope``` which is bound to current **Context** root **LifecycleStage**. In case if current **Context** terminated - ```Contextual.contextCoroutineScope``` will be cancelled. 

```Contextual.lifecycleCoroutineScope``` is an instance of ```CoroutineScope``` which is bound to current **Context** active **LifecycleStage**. Because of **LifecycleStage** can be entered and exited many times each time **LifecycleStage** is entered the new instance of ```Contextual.lifecycleCoroutineScope``` is created and when current **LifecycleStage** is exited - this ```Contextual.lifecycleCoroutineScope``` will be cancelled. 

This is used to reactive programming module of **apptronic.net/core** framework, but also can be used to launch coroutines by the developer.

These ```CoroutineScope```s cannot be cancelled manually, call ```CoroutineScope.cancel()``` will do nothing. Cancellation of these ```CoroutineScope```s is performed automatically by the framework.

### Managed CoroutineScope

class SomeComponent(context: Context) : BaseComponent(context) {

    val contextScope = createContextCoroutineScope()
    val stagedScope = createLifecycleCoroutineScope()

    fun runLocal() {
        contextScope.launch {
            // coroutine execution core
        }   
    }

    fun runOnCurrentStage() {
        stagedScope.launch {
            // coroutine execution core
        }   
    }
    
    fun cancelScopes() {
        contextScope.cancel()
        stagedScope.cancel()
    }

}

```Contextual.createContextCoroutineScope()``` creates a new instance of ```CoroutineScope``` which is bound to root **LifecycleStage** of current **Context**.

```Contextual.createLifecycleCoroutineScope()``` creates a new instance of ```CoroutineScope``` which is bound to currently active **LifecycleStage** of current **Context**, so it depends on moment when it called.

These scopes also have automatically managed cancellation by the framework, but in addition they can be cancelled manually.

##### Throttling

In some cases it needed to guarantee that actions executed one after another, preventing parallel executions.

In that case is possible to call ```CoroutineScope.throttler(Int)``` which returns an instance of ```CoroutineThrottler```, which uses ```CoroutineScope``` for launching coroutines, but handles invocation queue inside and launches next coroutine only after the previous coroutine completed its execution, preventing parallel executions.

The parameter passed is size limit of queue. In case of queue overflow the oldest not executed coroutine will be thrown out of queue.

Calling ```CoroutineScope.serialThrottler()``` will return ```CoroutineThrottler``` with queue of size Int.MAX_VALUE (meaning it practically infinite).

___

[Back to Manual](../manual.md)

Next topic: [Implementation of Components: reactive behavior](reactive_behavior.md)
