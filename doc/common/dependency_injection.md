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

```kotlin
interface SomeDependency

class SomeDependencyImpl : SomeDependency

interface SomeInterface

class SomeInterfaceImpl(val dependency: SomeDependency) : SomeInterface

val SomeModule = declareModule {
    single<SomeDependency> {
        SomeDependencyImpl()
    }
    factory<SomeInterface> {
        SomeInterfaceImpl(
            dependency = inject()
        )
    }
}
```

Technique above shows a simple way to inject constructor ot other parameters in **Module**.

All injections performed here are taken from current **Context** with all of it's **Modules** and **Instances** and all its parent **Contexts**.

### Injection Descriptors

Most Dependency Injection solutions provides some kind of "named" injection, when key for providing/injection of object is not an object type but some String key. In Java there is ```@Named``` annotation.

Problem of that solutions is that key does not contain information about type of provided/injected object.

Because of that **apptronic.net/core** framework contains another approach.

```kotlin
val ServerUrlDescriptor = createDescriptor<String>()
val MaxCountDescriptor = createDescriptor<Int>()
val ListOfNamesDescriptor = createDescriptor<List<String>>()

val Module = declareModule {
    single(ServerUrlDescriptor) {
        "https://some-server.com/"
    }
    single(MaxCountDescriptor) {
        256
    }
    single(ListOfNames) {
        listOf("Emma", "John", "Kate")
    }
}

val serverUrl = inject(ServerUrlDescriptor) // type: String
val maxCount = inject(MaxCountDescriptor) // type: Int
val names = inject(ListOfNamesDescriptor) // type: List<String>
```

Main advantages of this approach:
 * Descriptor contains type of provided/injected object.
    * Compiler will verify that provided object is corresponds descriptor type.
    * When injected type of object known by the compiler.
 * Descriptor key is a unique object. Two descriptors with same type and declared name is still different keys so there is no possibility of collision like when Strings have same content.
 * It's possible to create private descriptors and completely hide providing/injection out of your code.

### Using Component as injectable interface implementation

Classical approach for Dependency Injection is one of two ways:
 * use reflection to set value of field in some object
 * provide dependencies using constructors
Both approaches have sme problems. When reflection can cause issues with performance, usage of constructor can cause that number of parameters become too big.
```kotlin
interface Dependency1
interface Dependency2
interface Dependency3
interface Dependency4
interface Dependency5

interface SomeInterface

class SomeImplementation(
    private val dependency1: Dependency1,
    private val dependency2: Dependency2,
    private val dependency3: Dependency3,
    private val dependency4: Dependency4,
    private val dependency5: Dependency5
) : SomeInterface

val MyModule = declareModule {
    factory<SomeInterface> {
        SomeImplementation(
            dependency1 = inject(),
            dependency2 = inject(),
            dependency3 = inject(),
            dependency4 = inject(),
            dependency5 = inject()
        )
    }
}
```
Because of that we recommend to use **Component** as implementation class for any dependencies when possible.

```kotlin
interface Dependency1
interface Dependency2
interface Dependency3
interface Dependency4
interface Dependency5

interface SomeInterface

class SomeImplementation(
    context: Context
) : BaseComponent(context), SomeInterface {

    private val dependency1 = inject<Dependency1>()   
    private val dependency2 = inject<Dependency2>()   
    private val dependency3 = inject<Dependency3>()   
    private val dependency4 = inject<Dependency4>()   
    private val dependency5 = inject<Dependency5>()   

}

val MyModule = declareModule {
    factory<SomeInterface> {
        SomeImplementation(
            providedContext()
        )
    }
}
```

This approach allows to not use huge number of parameters in constructors and no uses reflection.

### Providing external instances into DependencyDispatcher

Sometimes it needed to add some external object to be used in Dependency Injection module.

It is possible to do it on ```DependencyDispatcher```:
```kotlin
interface SomeInterface

class SomeInterfaceImpl

val UrlDescritptor = createDescriptor<String>()

/**
* Create new [ContextDefinition] based on another [ContextDefinition] and then create [Context] with it
*/
val context: Context = EmptyContext.with {
    dependencyDispatcher.addInstance<SomeInterface>(SomeInterfaceImpl())
    dependencyDispatcher.addInstance(UrlDescritptor, "https://some-url.com")
}.createContext(parent)

/**
* Extend [ContextDefinition] from component constructor
*/
class SomeComponent(
    parent: Context, someInterface: SomeInterface, url: String
) : BaseComponent(
    parent, EmptyContext.with {
                dependencyDispatcher.addInstance<SomeInterface>(SomeInterfaceImpl())
                dependencyDispatcher.addInstance(UrlDescritptor, "https://some-url.com")
            }
)

class SomeOtherComponent(
    parent: Context, someInterface: SomeInterface, url: String
) : BaseComponent(
    parent, EmptyContext
) {

    /**
    * In this case init { ... } block should be first in class to guarantee that instances
    * will be added before any inject() call
    */
    init {
        context.dependencyDispatcher.addInstance(someInterface)
        context.dependencyDispatcher.addInstance(UrlDescritptor, url)
    }

}
```

It is recommended to use last approach with ```init{}``` block if it is possible.

### Injection of Context

```kotlin
class SomeClass(val context: Context)
val SomeSpecificContextDescriptor = createDescriptor<Context>() 

val SomeModule = declareModule {
    single<SomeClass> {
        SomeClass(context = inject()) // will throw IllegalArgumentException ALWAYS
    }
    single<SomeClass> {
        SomeClass(context = definitionContext()) // returns current [Context]
    }
    factory<SomeClass> {
        SomeClass(context = providedContext()) // returns [Context] where [inject] called
    }
    single<Context> { // will throw IllegalArgumentException ALWAYS
        definitionContext()
    }   
    single(SomeSpecificContextDescriptor) {
        // this registers typed [Descriptor]
        definitionContext()
    }   
    single<SomeClass> {
        SomeClass(context = inject(SomeSpecificContextDescriptor)) // returns specified [Context]
    }
}
```

As shown above injection or registering **Context** provider is impossible. This is because of working with **Context** without understanding can cause unexpected behavior.

Instead of that in any scope it possible to call ```definitionContext()``` which returns that **Context** in which **Module** is registered.

In addition, for ```factory``` scope it possible to call ```providedContext()``` which returns that **Context** instance in which ```inject()``` is called.

If be any case it still needed to inject some another **Context** it possible to use **Descriptor**. For descriptors there is no limitation with usage of ```Context``` type for injection.

### Creation of new instance of Context for provided object

If providing of instance in **Module** requires separate **Context** for provided object it is possible to create new context exaclty from its definition in **Module**.

```kotlin
class SomeSingleComponent(context: Context) : BaseComponent(context)
class SomeFactoryComponent(context: Context) : BaseComponent(context)
class SomeSharedComponent(context: Context) : BaseComponent(context)

val MyModule = declareModule {
    
    single {
        SomeSingleComponent(
            context = scopedContext(EmptyContext)
        )
    }

    factory {
        SomeFactoryComponent(
            context = scopedContext(EmptyContext)
        )
    }

    shared {
        SomeSharedComponent(
            context = scopedContext(EmptyContext)
        )
    }

}
```

```fun <T : Context> scopedContext(contextDefinition: ContextDefinition<T>``` available for all scopes mentioned in this sample:
 * Single scope: new context created with parent ```definitionContext()```
 * Factory scope: new context created with parent ```providedContext()```
 * Shared scope: new context created with parent ```definitionContext()```
Optionally it possible to provide parent context as second parameter to ```scopedContext()``` if default parent is no match required behavior.

Main purpose of usage of ```scopedContext()``` is that is closed automatically when corresponding scope ends.
___

[Back to Manual](../manual.md)