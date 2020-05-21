[Back to Manual](../manual.md)

Next topic: [Lifecycle](lifecycle.md)
___
## Creating Contexts and Components

**Component** is base class which can be used for implementation of any application parts: services, repositories, ViewModels (there is specific sub-class for that name **ViewModel**). This is not required to use **Component** class for all, but it's designed to simplify development of any behaviors inside app.

### Creating Core Context

```kotlin
val AppContext: Context = coreContext {
    // setup context
}
```
This **Context** generally is a single object which lives all time wile app process alive.

### Creating Components and custom Contexts

Create **Component** class

```kotlin
class MyComponent(context: Context) : BaseComponent(context) {

    // implementation

}
```

If it needed to create new **Context** for **Component** it needed to define new **Context** and use it:
```kotlin
val MyComponentContext: ContextDefinition<Context> = defineContext {
    // setup context
}
```
Code above shown how to create **ContextDefinition**: object, which holds description of context.

**ContextDefinition** can be used to create context using this definition using any other context as parent.
To simplify this code it possible to create **Component**
```kotlin
fun createComponent(parent: Context) : MyComponent {
    val context: Context = MyComponentContext.createContext(parent)
    return MyComponent(context)
}
```
Another way to create component is to replace **Context** of component to its parent and **ContextDefinition**:
```kotlin
class MyComponent(parent: Context) : BaseComponent(parent, MyComponentContext) {

    // implementation

}
```
In that case **Component** will create new **Context** based on **ContextDefinition** with specified parent **Context**.

For testing purposes it may be needed to specify both constructors:
```kotlin
class MyComponent : BaseComponent {
    
    // for use in production code: using specified [ContextDefinition]
    constructor(parent: Context) : super(parent, MyComponentContext)

    // for use in unit test: can provide any context, created for test purposes
    constructor(context: Context) : super(context)

    // implementation

}
```
If **Component** is "helper" (which may work in any **Context** and do not require separate **Context** to work) you should always provide concrete **Context** in constructor:
```kotlin
class HelperComponent(context: Context) : BaseComponent(context)
```
In that case code of initialization will be like this:
```kotlin
class MyComponent(parent: Context) : BaseComponent(parent, MyComponentContext) {

    val helper = HelperComponent(
        this.context // [Context] object always accessible inside of [Component]
    )

    // implementation

}
```
In this case class ```MyComponent``` defines "main" **Component**, which hold logic, when ```HelperComponent``` is a dependency of ```MyComponent```.

In production code better to use [Dependency Injection](dependency_injection.md) for combining **Components** and providing mock instances for testing.

If **Component** does not require **Context** with specific definition (no DI modules, no custom Lifecycle) if possible to use predefined **EmptyContext** definition.
```kotlin
class MyComponent(parent: Context) : BaseComponent(parent, EmptyContext)
```
___
[Back to Manual](../manual.md)

Next topic: [Implementation of Componentns, threading(coroutines) and reactive behavior](threading_and_reactive_behavior.md)