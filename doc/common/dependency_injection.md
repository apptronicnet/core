[Back to Manual](../manual.md)

Previous topic: [App UI implementation with ViewModels](view_models.md)

___

## Using Dependency Injection

**apptronic.net/core** contains integrated Dependency Injection API.

### Creating modules and defining dependencies

Let's define some interface and its implementation which needed to be used for injection:
```kotlin
interface MyInterface {

    fun someMethod(): String

}

class MyInterfaceImpl : MyInterface {

    override fun someMethod(): String {
        return "Hello from DI!"
    }

}
```
Then let's declare a module:
```kotlin
val CustomModule = declareModule {

    // we define that this is singleton instance in scope of [Context]
    // <MyInterface> is dependency declaration, which can be used later fo injection
    single<MyInterface> {
        MyInterfaceImpl()
    }

}
```
And create some custom context:
```kotlin
val MyContext = defineContext {
    dependencyDispatcher.addModule(CustomModule)
}
```
After this any **Component**, which uses ```MyContext``` or <ins>any child **Context**</ins> will be able to inject ```MyInterface```.

```kotlin
class MyComponent(parent: Context) : BaseComponent(parent, MyContext) {
    
    // Same dependency declaration <MyInterface> as in [CustomModule]
    val myInterface = inject<MyInterface>()
    
    // implementation, where we can use [myInterface] instance

}
```

### Working with scopes

Scopes in **apptronic.net/core** managed automatically by **Context** and **Lifecycle**, so there is no any code needed from developer side to manage them.

There are 3 types of scope available in **Module** declaration:

##### Single scope

```kotlin
val CustomModule = declareModule {

    single<MyInterface> {
        MyInterfaceImpl()
    }

}
```

Declares, that instance of ```MyInterfaceImpl``` created only once when providing of ```MyInterface``` requested first time. Any next request for providing of ```MyInterface``` will return same instance of ```MyInterfaceImpl```.

Scope bound to **Context** lifecycle and destroyed only when **Context** destroyed. It means, that for creation of classical global *Singleton* it needed to use global **Context** - **Core Context**. In other cases instance will be a *Singleton* for that **Context** in which **Module** was added and all its child **Contexts**.

```kotlin
class MyComponent(parent: Context) : BaseComponent(parent, MyContext) {
    
    val ref1 = inject<MyInterface>()
    val ref2 = inject<MyInterface>()
    val ref3 = inject<MyInterface>()
    
    init {
        println("${ref1 === ref2}") // true
        println("${ref2 === ref3}") // true
        println("${ref1 === ref3}") // true
    }

}
```
##### Factory scope

```kotlin
val CustomModule = declareModule {

    factory<MyInterface> {
        MyInterfaceImpl()
    }

}
```

Declares, that instance of ```MyInterfaceImpl``` will be created for each providing request for ```MyInterface```. For each new injection request the new instance of ```MyInterfaceImpl``` will be created.

Scope bound to **Context** and is active **LifecycleStage** from which injection it called, not from **Context** where **Module** with declaration added. Meaning, that if injection called from some child **Context** then it will be bound to that chold **Context** active **Lifecycle Stage**.

```kotlin
class MyComponent(parent: Context) : BaseComponent(parent, MyContext) {
    
    val ref1 = inject<MyInterface>()
    val ref2 = inject<MyInterface>()
    val ref3 = inject<MyInterface>()
    
    init {
        println("${ref1 === ref2}") // false
        println("${ref2 === ref3}") // false
        println("${ref1 === ref3}") // false
    }

}
```

##### Shared scope

```kotlin
val CustomModule = declareModule {

    shared<MyInterface> {
        MyInterfaceImpl()
    }

}
```

Declares, that instance of ```MyInterfaceImpl``` will be created for each providing request for ```MyInterface``` when there is no previously created and not recycled instance. It guarantees, that at same time all injections will return same instance, but after all injection scopes ended the instance will be recycled and next injection will create new shared instance.

Scope holds inside of **Context** where **Module** is added but <ins>not</ins> bound directly to it's **Lifecycle** or **Lifecycle Stage**. 

```kotlin
class MyComponent(parent: Context) : BaseComponent(parent, MyContext) {
    
    val instances = mutableListOf<MyInterface>()
    
    init {
        onEnterStage(Stage1) {
            val ref1 = inject<MyInterface>()
            // instance added to list each time [Lifecycle] entering [Stage1]
            instances.add(ref1)
            onEnterStage(Stage2) {
                val ref2 = inject<MyInterface>()
                onEnterStage(Stage3) {
                    val ref3 = inject<MyInterface>()
                    println("${ref1 === ref2}") // true
                    println("${ref2 === ref3}") // true
                    println("${ref1 === ref3}") // true
                }
            }
        }

        enterStage(context, Stage1)
        enterStage(context, Stage2)
        enterStage(context, Stage3)
        exitStage(context, Stage3)
        exitStage(context, Stage2)
        exitStage(context, Stage1)

        println("${instances.size}") // 1
 
        enterStage(context, Stage1)
        enterStage(context, Stage2)
        enterStage(context, Stage3)
        exitStage(context, Stage3)
        exitStage(context, Stage2)
        exitStage(context, Stage1)

        println("${instances.size}") // 2

        println("${instances[0] === instances[1]}") // false

   }

}
```
As shown in code above, when injection of ```MyInterface``` is performed after entering ```Stage1```, and then after entering ```Stage2``` and ```Stage3``` it returned the same instance.

But after exiting and entering all **Lifecycle Stages** again another instance of ```MyInterface``` returned, which again same for all **Lifecycle Stages**, but not same which was returned after first **Lifecycle Stages** enter-exit flow, meaning previous instance was recycled and it's scope ended.

This type of Scope is designed for cases, when required that all clients will use same instance on same time, but not needed to keep this instance when it not needed (for example some *Event bus* service)

### Injection inside Module declaration

TBD

### Providing external instances into DependencyDispatcher

TBD

___

[Back to Manual](../manual.md)