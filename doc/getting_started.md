Read [Manual](manual.md) for complete guide to framework.

___

### Create project

First open Intellij Idea / Android Studio and start Kotlin Multiplatform project

##### Setup build.gradle

```groovy
kotlin {
    android("android")
    sourceSets {
        commonMain {
            dependencies {
                implementation kotlin('stdlib-common')
                implementation kotlin('reflect')
                implementation "net.apptronic.core:core-commons:${version}"
                implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.3.5"
            }
        }
        androidMain {
            dependencies {
                implementation kotlin('stdlib')
                implementation "net.apptronic.core:core-android:${version}"
                implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.5"
                implementation "androidx.recyclerview:recyclerview:1.1.0" // for lists
                implementation "androidx.viewpager:viewpager:1.0.0" // for pages
            }
        }
    }
}
```

### Create common code

AppContext.kt

```kotlin
// this class is renposible for declaring core app Context, which will be alive while
// app itself alive
val AppContext = coreContext {
     // later you can add initialization of DI modules here
}
```

AppComponent.kt

```kotlin
// this class is declaration of Core-level component, responsible for app behavior
class AppComponent(context: Context) : BaseComponent(context) {

    // this item is managing core app UI model (AppViewModel for these case)
    // it is used by UI side to retrieve ViewModel and recycle it when UI closed
    val appUI = viewModelDispatcher {
        AppViewModel(it)
    }
            
}
```

AppViewModel.kt

```kotlin
// this class contains application UI model.
// Inner navigation implemented with Navigators (ListNavigator, StackNavigator)
class AppViewModel(parent: Context) : ViewModel(parent, EMPTY_VIEW_MODEL_CONTEXT) {

    val message = value("Hello from apptronic.net Core")

    fun onBackPressed() {
        // later we can intercept system back pressed button events here (for Android)
        // we can create same methods for each platform which have system-generated events
        return false
    }

    // implementation

}
```

### Create Android app implementation

App implementation uses common code and
- binds Activity to AppViewModel
- maintains View side (layout and bindings) to correctly display changes of UI view model

App.kt

```kotlin
class App : Application() {
    
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        // before initialization of AppComponent we need to complete AppContext initialization
        // for Android we may add module with implementations of platform-specific features, declared in
        // common code as interfaces
        // also we need to install Android plugin, which extend framework to work with it's Android binding library
        AppContext.installAndroidApplicationPlugin(this) {
            // this specifial common view factory to be used in all bindings
            viewFactory(AppViewFactory)
            // this specifies binding between Activity and concrete ViewModel type
            // last parameter is optional invocation for system onBackPressed button
            bindActivity(MainActivity::class, AppViewModel::class) {
                it.onBackPressed()
            }
        }
        // last is creating AppComponent and storing it inside Application class to prevent from garbage collection
        appComponent = AppComponent(AppContext)
    }


}
```

app.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        android:id="@+id/textView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_centerInParent="true"
        android:gravity="center"
        android:textSize="24sp" />

</RelativeLayout>
```


AppView.kt

```kotlin
// this class declares View layer for concrete ViewModel type
class AppView : AndroidView<AppViewModel>() {

    // baseic declaration of some layout to be used
    override var layoutResId: Int? = R.layout.app_view

    override fun onBindView(view: View, viewModel: AppViewModel) {
        with(view) {
            // lets bind content of TextView to data inside VideModel
            +(textView setTextFrom viewModel.message)
            // do bindings
        }
    }

}
```

AppViewFactory.kt

```kotlin
// this object is registry ob View to ViewModel bindings
// it was used on installation of Android plugin and will be used by detault in all navigators
// and for Activity bindings
// it is possible to override it for concrete Navigator biding if needed
val AppViewFactory = androidViewFactory {
    addBinding(::AppView)
}
```

MainActivity.kt

```kotlin
class MainActivity : AppCompatActivity() {

    // nothing needed to write here as all integration done by plugin
    // setContentView() also called by plugin
    // but do not forget to add it to AndroidManifest.xml

    // it is possible to made manual integration but not needed for most of cases
    // what may be needed it window initialization and other features

}
```

___

Read [Manual](manual.md) for complete guide to framework.