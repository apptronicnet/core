Compatibility library for migration of existing applications to apptronic.net core Framework.

Migration steps:

1. Migrate Dependency Injection

This is first stage as all parts of apptronic.net/core well-integrated with it's Dependency Injection framework and next stages will be much easier.

After this part all your app parts will have declared own Context (this is NOT Android Context class): the object which defines "responsibility area" for each component and declares dependencies, lifecycle and other features required for apptronic.net/core Components.

Compatibility library allows to easily interact with Dependency Injection from Activities, Fragments and other Android components.

2. Migrate app components to apptronic.net/core framework
 - use apptronic.net/core ViewModel framework instead of androidx/ViewModel
 - use apptronic.net/core Component framework for services, repositories etc.
 - use Kotlin/Coroutines and apptronic.net/core Reactive framework instead of threading, RxJava, LiveData etc.
 
During this stage all app components logic will be moved to apptronic.net/core Components (as ViewModel is sub-type of Component making it easier to implement it as it generally developed in same way as other Components).

The apptronic.net/core contains integration with Kotlin/Coroutines providing easy access to Context-bound CoroutineScopes and CoroutineContexts (Dispatchers etc.).

The apptronic.net/core Reactive framework also integrated with Kotlin/Coroutines and ip to 99.9% of practical cases when RxJava used will be easily converted to it. 0.1% of other cases will simply require different approach and some logical refactoring.

Advantages of apptronic.net/core Reactive framework is:
 - performance: because it generally works in Main thread and there is no need to thread synchronization and other performance-cost operations
 - easy debugging: all stack traces will show from which point it invoked (except cases of asynchronous transformations or Coroutines)
 - no threading bugs: as it work in Main thread there is no issues with incorrect thread used for delivering events
 - Coroutines integration: any transformation can be moved to coroutine which can execute any code block using CoroutineDispatcher meaning it's execution in background threads
 - Fully multi-platform: reuse all code later for iOS
 - easy covered by unit tests: injection of dispatchers for testing through Context for simulating background executions during test run

3. Migrate Fragments to ViewBinderFragments to use ViewBinder for UI binding.

During this process Fragments become only hosts for pair ViewModel + ViewBinder, meaning no logical code will be inside fragments.

This stage is preparing for removing Fragments from application at all.

4. Refactor navigation with apptronic.net/core Navigation framework.

After this stage Fragment framework will be removed from application and all navigation and state management will be implemented using apptronic.net/core features.

5. Making app code multiplatform.

The apptronic.net Framework are completely multiplatform. Now all platform-dependent components can be moved to interfaces and injected via Dependency Injection, expect/actual classes or any other way into app core code.

After that whole app core code becomes ready to be moved into Kotlin/Multiplatform common source set (including ViewModels and navigation) making up to 90% of app code reusable for iOS and other supported platforms.

Only UI (layouts and other resources), ViewBinders and platform specific features (database, permissions, hardware access) will be written on native code and injected into app core.

This approach allows fully native interaction with platform including native platform UI, but at same time developing whole app core with all UI and business logic in common code.