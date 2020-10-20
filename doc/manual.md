# apptronic.net/core
### Modern framework for developing Multiplatform apps

It needed to create [Kotlin Multiplatform project](https://kotlinlang.org/docs/reference/) to use Core Framework.

To easy setup **apptronic.net/core** framework in project read [getting started Guide](getting_started.md)

Demo app which demonstrates basics of **apptronic.net/core**:<br/>[https://github.com/apptronicnet/core_demo_android_app](https://github.com/apptronicnet/core_demo_android_app)

### Documentation

How to implement app architecture with **apptronic.net/core** framework

#### Framework architecture

[Framework Architecture](common/architecture.md)

[Lifecycle](common/lifecycle.md)

[Creating Contexts and Components](common/components.md)

[Implementation of Components: threading(coroutines)](common/threading.md)

[Implementation of Components: reactive behavior](common/reactive_behavior.md)

[Implementation of Components: advanced reactive behavior](common/reactive_behavior_advanced.md)

[App UI implementation with ViewModels](common/view_models.md)

[Using Dependency Injection](common/dependency_injection.md)

#### Android

TBD: Project initialization and Android plugins

[Android: implement layout and bindings](common/android_layout_bindings.md)

TBD: Using compatibility mode with Fragments

#### Advanced features

TBD ViewModel: features // Overview of all **ViewModel** features for implementation custom behavior

TBD ViewModel binding: specific models and binding types // Pre-defined typed on binding models for common purposes

TBD ViewModel binding: implement custom binding // Implementation of custom models and bindings

TBD Navigation: StackNavigator // Classic navigation: back, forward, replace

TBD Navigation: ListNavigator // Easily create list of any items

TBD Navigation: RecyclerListNavigator // How to implement large lists or create dynamic lists with advanced behavior

TDB Using ViewModelDispatcher // Managing or root **ViewModel** and its lifecycle

TBD Context Plugins // How to extend functionality of **apptronic.net/core** inside your app

TBD Defining custom lifecycle // How to create and use custom **Lifecycle** for **Context**

TBD Dependency Injection: injection with parameters // Adding injection parameters from place where ```inject()``` called

TBD Dependency Injection: debugging // How to solve issues with Dependency Injection

TBD Dependency Injection: lazy injections // How to use ```lazy``` for ```inject()``` call

TBD Dependency Injection: recycling // Implement recycling of instances, provided by **Modules**

TBD Composite ViewModels and bindings // Creating multiple *ViewModels* inside single **ViewModelContext**

TBD Working with suspend transformations with coroutines // How to work with reactive streams in different threads (with Coroutines)

TBD Reactive streams features // Special functions for reactive streams: ```debounce```, ```throttle``` etc...

TBD Reactive streams: watcher // How to react on specific states of stream values

TBD Implementation of custom Entity classes // How to create custom reactive stream function or transformation
