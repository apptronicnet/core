[Back to Manual](../manual.md)

Previous topic: [Implementation of Components: threading(coroutines)](threading.md)
___

## Implementation of Components: reactive behavior

**apptronic.net/core** framework contains integrated reactive library.

##### Base architecture

The main brick or reactive is ```Entity```
```kotlin
interface Entity<T> : Observable<T> {

    val context: Context

    override fun subscribe(observer: Observer<T>): EntitySubscription {
        return subscribe(context, observer)
    }

    fun subscribe(context: Context, observer: Observer<T>): EntitySubscription

}
```
Entity is ```Observable``` which is bound to ```Context``` and automatically works with it's ```Lifecycle```.

This is main difference from classical ```Observable``` is that it contains reference to the ```Context``` in which it was created and each subscription, created by invocation of ```subscribe()``` method, automatically bounds to current active ```LifecycleStage``` of it's ```Context``` (not when ```Entity``` created, but when ```subscribe()```), being automatically cancelled when ```LifecycleStage``` is exited.

![Entity subscription](../images/entity_subscription.svg)

1. Lifecycle Stage entering triggers create subscription by calling ```Entity.subscribe()``` method.
2. ```Entity``` creates subscription and automatically registers it in currently active ```LifecycleStage```.
3. When specified ```LifecycleStage``` exiting - ```EntitySubscription.unsubscribe()``` called automatically.

##### Working with different Contexts

Each ```Entity``` always bound to concrete ```Context```. It means that all ```subscribe()``` calls will register subscription in ```Lifecycle``` of that ```Context```.

But in many cases it needed to create subscription from some another place, which is working in another ```Context```.

For that case there is second variant for subscribe:
```kotlin
fun subscribe(context: Context, observer: Observer<T>): EntitySubscription
```
Mechanics of that differs from call without context specification:
* In case if parameter ```context``` same as ```Entity``` own ```context``` the subscription management will work in same way.
* In case if parameter ```context``` differs from ```Entity``` own ```context``` the subscription will be registered in both ```Context```, but with some differences:
    * in **source** ```Context```, which is ```Entity``` own ```Context```, it registered not it active ```LifecycleStage```, but in root ```LifecycleStage```, meaning automatic unsubscribe will be triggered only when **source** ```Context``` is terminated.
    * in **target** ```Context```, which is provided as parameter ```context: Context``` to ```subscribe``` method, it registered it's active ```LifecycleStage``` and will be automatically unsubscribed when currently active ```LifecycleStage``` of **target** ```Context``` exits.

<ins>This is very important to keep in mind this when using ```Entity``` not inside of same component, but provided externally from any other place.</ins>

##### Switching Entity context

Not always it needed to use ```subscribe()```, sometimes ```Entity``` used as source for transformations. In that case, instead of using
```kotlin
fun subscribe(context: Context, observer: Observer<T>): EntitySubscription
```
it needed to use
```kotlin
fun <T> Entity<T>.switchContext(targetContext: Context): Entity<T>
```
This function creates new ```Entity``` in ```targetContext```, which reflects all changes from source ```Entity``` and allows use it in ```targetContext```.

For example:

```kotlin
interface SomeRepository {

    fun someData(target: Context) : Entity<SomeDataType>

}

class SomeRepositoryImpl(context: Context) : BaseComponent(context) {

    private val someDataSource: Entity<SomeDataType> // some implementation

    override fun someData(target: Context) : Entity<SomeDataType> {
        return someDataSource.switchContext(target)
    }

}

class ClientComponent(context: Context) : BaseComponent(context) {

    val someRepository = inject<SomeRepository>()

    val someDataRepresentation = someRepository.someData(context).map {
        // implement mapping function
    }   

}
```

The example above shows correct usage of ```Entity``` which needed to be transferred between different **Components**.

As each **Context** is <ins>isolated</ins> responsibility zone, transferring reactive streams between any **Contexts** requires creation of *bridge* between this **Contexts**. Function ```switchContext(targetContext: Context)``` creates that bridge using ```subscribe(context: Context)``` internally on source ```Entity```.

As injection or another way of providing of any external object means no knowledge of its scope and **Context** interface which provides any function which returns ```Entity``` or it's sub-type should require ```Context``` as parameter and call ```switchContext(targetContext: Context)``` in its implementation.

![Switching entity context](../images/switch_entity_context.svg)

Ignoring this rule may cause unexpected behavior of automatic subscription management, as any actions, performed with ```Entity``` from another **Context**, returned without calling ```switchContext(targetContext: Context)```, will be bound to its own **Context**, depending on its **Lifecycle**, active **LifecycleStage** at the moment of any subscribe/transform call and will not know anything about target **Context**.

One more thing is that any transformation function which uses several **Entities** as its source will fail with runtime Exception as it uses source **Entities** **Context** as its own **Context**. If source **Entities** have different **Contexts** it cannot define which **Context** to use. But when all sources uses same **Context** it will work normally, independently of this **Entities** created in this **Context** or bridget to this **Context** using ```Entity.switchContext(targetContext: Context)``` call.

##### Declaring Entity

TBD

##### Entity transformation functions

TBD

##### Implement custom Entity of transformation function

TBD

##### Asynchronous transformation with Coroutines

TBD

___

[Back to Manual](../manual.md)

Next topic: [App UI implementation with ViewModels](view_models.md)