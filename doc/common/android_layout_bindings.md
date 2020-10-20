[Back to Manual](../manual.md)

___

## Android: implement layout and bindings

Framework uses native Android view layer for rendering. It means, from one hand, that it needed to write Android-specific UI code, but from other hand, that ot possible to handle any native Android UI interactions without pain.

#### ViewBinder

The main class for implementing Android view layer is ```ViewBinder```.

Create Android xml-layout:

```xml
<?xml version="1.0" encoding="utf-8"?>
<TableLlayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        android:id="@+id/textView" />

    <EditText
        android:id="@+id/editText" />

    <Button
        android:id="@+id/button"
        android:text="My button" />

</TableLlayout>
```

Implement ViewModel and corresponding ViewBinder:

```kotlin
class MyViewModel(context: ViewModelContext) : ViewModel(context) {

    // Declare some text message which should be displayed to user.
    val someMessage = value<String>()
    
    // Message color. CoreColor is multi-platform color model.
    val messageColor = value<CoreColor>(CoreColor.BLACK)

    // Text input fiend model.
    val input = textInput("")

    // To handle click events.
    fun onClick() {
        // handle click
    }

    // behavior implementation

}

class MyViewBinder : ViewBinder<MyViewModel>() {

    override var layoutResId: Int? = R.layout.my_layout

    override fun onBindView(view: View, viewModel: MyViewModel) {

        // Show text from viewModel.someMessage in TextView with id=textView
        bindText(view.textView, viewModel.someMessage)

        // There is no ready to use binding for CoreColor.
        // So simply subscribe on it and call TextView.setTextColor()
        // CoreColor.colorInt return Android color integer
        // IMPORTANT!!! No unsubscribe needed. It is automativally handled by ViewModel lifecycle.
        viewModel.messageColor.subscribe { it: CoreColor ->
            view.textView.setTextColot(it.colorInt)
        }

        // Bind viewModel.input and EditText with id=editText.
        // After that all changes from one of these always represented on another.

        bindTextInput(view.editText, viewModel.input)
        // Bind viewModel.onClick() method to be invoked when user clicks on Button with id=button
        bindClickListener(view.button, viewModel::onClick)

    }

}
```

The example above shows the common way for creating and binding view layer to **ViewModel**. For each single **ViewModel** the corresponding ```ViewBinder``` should be created.

#### Managing ViewBinder's in project

It needed to tell **apptronic.net/core framework** to use this ```ViewBinder```.

```kotlin
val AppBinderFactory = viewBinderFactory {
    add(::MyViewBinder)
    add(::ViewModel2ViewBinder)
    add(::ViewModel3ViewBinder)
    add(::ViewModel4ViewBinder)
    // and all other binders
}
```

```AppBinderFactory``` is the object which creates ```ViewBinder``` by request for each concrete ```ViewModel```.

Now it needed to pass ```AppBinderFactory``` to framework:

```kotlin
class App : Application() {
    
    override fun onCreate() {
        super.onCreate()
        AppContext.installAndroidApplicationPlugin(this) {
            binderFactory(AppBinderFactory)
        }
    }

}
```

And that is all! Now each time when **apptronic.net/core framework** tries to render any **ViewModel*8 it asks ```AppBinderFactory``` to provide the corresponding instance for it.

Base plugin setup described in [Getting started Guide](../getting_started.md).

#### Binding navigators

Let's create some container in which it needed to switch some view content (for example, switch app screens).

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/container"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

Then create navigation model (```NavigationViewModel```) and ```NavigationViewBinder``` for it.

```kotlin
class NavigationViewModel(context: ViewModelContext) : ViewModel(context) {

    val navigator = stackNavigator()
    
    init {
        // set initial [ViewModel] in [navigator]
        // someViewModel() is builder method which returns some [ViewModel]
        navigator.set { someViewModel() }
    }  

    fun onOpenAnotherViewModel() {
        // add new screen over first, passing previous visible [ViewModel] in back stack
        // anotherViewModel() is builder method which returns some [ViewModel]
        // BasicTransition.Forward is transition specification. View layer will create corresponding animation and play it when ciew changes.
        navigator.add(BasicTransition.Forward) { anotherViewModel() }
    }

    fun onNavigateBack() {
        // BasicTransition.Backward is transition specification. View layer will create corresponding animation and play it when ciew changes.
        navigator.popBackStack(BasicTransition.Backward)
    }

}

class NavigationViewBinder : ViewBinder<NavigationViewModel>() {

    override var layoutResId: Int? = R.layout.navigation_layout

    override fun onBindView(view: View, viewModel: NavigationViewModel) {
        // this will bind container to viewModel.navigator.
        // All changes in viewModel.navigator will be displayed inside of view.container
        bindNavigator(view.container, viewModel.navigator)
    }

}
```

And...that is all! Again. No more code needed - all navigation UI will be handled by framework, when developer controls it from ```NavigationViewModel```.

Here is example **ViewModel** with all common navigator types:

```kotlin
class NavigationExamplesViewModel(context: ViewModelContext) : ViewModel(context) {

    // basic navigator based on stack and shows last [ViewModel]
    val stackNavigator = stackNavigator()
    // navigator which manages list of [ViewModel]s and shows single [ViewModel] by index
    val selectorNavigator = selectorNavigator()
    // navigator which manages list of [ViewModel]s and can shown any number of [ViewModel]s at same time
    val listNavigator = listNavigator()
    // same as previous but [ViewModel]s are dynamically generated (for large lists)
    // source is Entity<List<T>>
    // Builder is implementation of ViewModelBuilder which dynamically creates [ViewModel] for item of type T
    val listDynamicNavigator = listDynamicNavigator(source, Builder)

}
```

There is number of predefined navigation bindings:

```kotlin
class NavigationExamplesViewBinder : ViewBinder<NavigationExamplesViewModel>() {

    override fun onBindView(view: View, viewModel: NavigationViewModel) {

        // container is any ViewGroup with ability to directly add/remove children: FrameLayout, RelativeLayout etc.
        bindNavigator(view.container, viewModel.stackNavigator)
        bindNavigator(view.container, viewModel.selectorNavigator)

        // recyclerView is RecyclerView
        // don't forget to set layoutManager (any: linear, grid etc.)
        view.recyclerView.layoutManager = LinearLayoutManager(view.context)
        bindNavigator(view.recyclerView, viewModel.listNavigator)
        bindNavigator(view.recyclerView, viewModel.listDynamicNavigator)

        // viewPager is ViewPager
        bindNavigator(view.viewPager, viewModel.listNavigator)
        // viewPager CANNOT work with listDynamicNavigator

        // viewPager2 is ViewPager2
        bindNavigator(view.viewPager2, viewModel.listNavigator)
        bindNavigator(view.viewPager2, viewModel.listDynamicNavigator)
        // viewPager2 CAN work with listDynamicNavigator
    }

}
```

#### Binding navigators: additional parameters

##### StackNavigator and SelectorNavigator: SingleViewMode

```kotlin
// container is any ViewGroup with ability to directly add/remove children: FrameLayout, RelativeLayout etc.
bindNavigator(view.container, viewModel.stackNavigator,
    SingleViewMode(
        binderFactory = CustomBinderFactory,
        transitionFactory = CustomTransitionFactory,
        defaultAnimationTime = 1000L
    )
)
bindNavigator(view.container, viewModel.selectorNavigator,
    SingleViewMode(
        // same parameters
    )
)
```

```SingleViewMode``` defines that one view displayed at time and other views destroyed immediately.
- ```binderFactory``` overrides default ```ViewBinderFactory``` (```AppBinderFactory```). If CustomBinderFactory don't provide ```ViewBinder``` then default ViewBinderFactory will be used as fallback.
- ```transitionFactory``` overrides default ViewTransitionFactory. If it not provided transition then default ViewTransitionFactory will be used as fallback.
- ```defaultAnimationTime``` specifies base animation time for transitions.

All parameters in ```SingleViewMode``` are optional, and defaults will be used if no passed.

##### StackNavigator and SelectorNavigator: SingleViewListMode

```kotlin
// container is any ViewGroup with ability to directly add/remove children: FrameLayout, RelativeLayout etc.
bindNavigator(view.container, viewModel.stackNavigator, 
    SingleViewListMode(
        binderFactory = CustomBinderFactory, // same as for SingleViewMode
        transitionFactory = CustomTransitionFactory, // same as for SingleViewMode
        defaultAnimationTime = 1000L, // same as for SingleViewMode,
        // count of currently not displayed views which can be cached.
        // Pass SingleViewListMode.CACHE_ALL to cache all views without limit.
        maxCachedViews = 5
    )
)
bindNavigator(view.container, viewModel.selectorNavigator, 
    SingleViewListMode(
        // same parameters
    )
)
```

```SingleViewListMode``` SingleViewListMode defines that one view displayed at time but other views are cached and reused when possible.
- ```binderFactory``` same as for ```SingleViewMode```.
- ```transitionFactory``` same as for ```SingleViewMode```.
- ```defaultAnimationTime``` same as for ```SingleViewMode```.
- ```maxCachedViews``` is count of currently not displayed views which can be cached and reused later. Pass ```SingleViewListMode.CACHE_ALL``` to cache all views without limit.

All parameters in ```SingleViewListMode``` are optional, and defaults will be used if no passed.

##### ListNavigator and ListDynamicNavigator with RecyclerView: ViewListMode

```kotlin
view.recyclerView.layoutManager = LinearLayoutManager(view.context)
bindNavigator(view.recyclerView, viewModel.listNavigator,
    ViewListMode(
        binderFactory = CustomBinderFactory, // same as for SingleViewMode and SingleViewListMode,
        styleAdapter = MyStyleAdapter(), 
        bindingStrategy = BindingStrategy.MatchRecycle
    )
)
bindNavigator(view.recyclerView, viewModel.listDynamicNavigator, 
    ViewListMode(
        // same parameters
    )
)
```

- ```binderFactory``` same as for ```SingleViewMode``` and ```SingleViewListMode```.
- ```styleAdapter``` allows to pre-process views in list: change backgrounds, layout constraints etc.
- ```bindingStrategy``` defines strategy for bind/unbind action. Read BindingStrategy source code comment for details.

All parameters in ```ViewListMode``` are optional, and defaults will be used if no passed.

##### ListNavigator with ViewPager: ViewPagerMode

```kotlin
bindNavigator(view.viewPager, viewModel.listNavigator, 
    ViewPagerMode(
        binderFactory = CustomBinderFactory, // same as for all other modes
            // 
        titleFactory = MyTitleFactory(),
        styleAdapter = MyStyleAdapter() // same as for ViewListMode
    )
)
```

- ```binderFactory``` same as for ```SingleViewMode``` and ```SingleViewListMode```.
- ```titleFactory``` provides ability to return titles for each page (may be needed for tabs etc.)
- ```styleAdapter``` same as for ```ViewListMode```.

All parameters in ```ViewPagerMode``` are optional, and defaults will be used if not passed.

IMPORTANT: ViewPager CANNOT bind to ListDynamicNavigator. This is caused by that fact that ViewPager required from adapter to support call indexOfItem() which can be too complex for large lists.

##### ListNavigator and ListDynamicNavigator with ViewPager2: ViewPager2Mode

```kotlin
bindNavigator(view.viewPager2, viewModel.listNavigator, 
    ViewPager2Mode(
        binderFactory = CustomBinderFactory, // same as for ViewListMode
        styleAdapter = MyStyleAdapter(), // same as for ViewListMode
        bindingStrategy = BindingStrategy.MatchRecycle // same as for ViewListMode
    )
)
bindNavigator(view.viewPager2, viewModel.listDynamicNavigator, 
    ViewPager2Mode(
        // same parameters
    )
)
```

- ```binderFactory``` same as for ```ViewlIstMode```.
- ```styleAdapter``` same as for ```ViewlIstMode```.
- ```bindingStrategy``` same as for ```ViewlIstMode```.

All parameters in ```ViewPagerMode2``` are optional, and defaults will be used if not passed.

ViewPager2 uses RecyclerView inside, so all parameters and functionality are same as for using it with RecyclerView.

Decause of it uses RecyclerView inside it is POSSIBLE to use ListDynamicNavigator with ViewPager2.

___

[Back to Manual](../manual.md)
