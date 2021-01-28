[Back to Manual](../manual.md)

Previous topic: [Framework Architecture](architecture.md)

___

## Lifecycle

As it described in the previous topic, any **Context** have its own lifecycle. Lifecycle or child Context is independent
of its parent Context, except termination: the termination of **Context**s **Lifecycle** causes termination of all
child **Context**s with their **Lifecycle**s independently of their state.

So, parent-child **Context** dependencies organization can dramatically change behavior for application. For
**ViewModel**s all inner **ViewModel**s always children of container **ViewModel** and all of them automatically
terminated with its parent. For all other places parent-child dependency is responsibility of developer.

**Lifecycle** is like a stack of **LifecycleStage**s. Each **Lifecycle** always contains **Root state** and comes
initially entered to it. Exiting from **Root state** is a **Context** termination.

Each **Lifecycle Stage**, including **Root State**:
- can be used to register enter/exit callbacks. It is useful to activate some behavior only when user see screen (for example, enable timer only when user see view)
- can be used to trigger one-time actions on enter (for example, it useful when result of some long-running action comes when app in background, when user should explicitly see completion event - one-time action then registered in *Visible* stage of **ViewModel**s **Lifecycle**)
- all **Entity** subscriptions, created by ```subscube``` calls, automatically registered in currently active **Lifecycle Stage** of **Entity** **Context** and unsubscribed on **Lifecycle Stage** is exited. So, all reactive behavior, based on **Entity**, which is started on entering **Lifecycle Stage** will be automatically stopped on the exit from this **Lifecycle Stage** because all reactive chains unsubscribed automatically. 

___

[Back to Manual](../manual.md)

Next topic: [Creating Contexts and Components](components.md)