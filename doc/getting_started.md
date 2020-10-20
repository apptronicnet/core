Read [Manual](manual.md) for complete guide to framework.

___

### Get the template project

The template project can be downloaded from [https://github.com/apptronicnet/mpp_app_template](https://github.com/apptronicnet/mpp_app_template).

It contains fully set up Kotlin/Multiplatform project with:
 - Android and iOS targets 
 - dependencies
 - basic stack navigation implementation with MVVM with Android UI
 - sample unit tests

##### Source structure

```
src
|---commonMain    // common code reused for Android and iOS
|   |---kotlin
|        |---apptemplate.apptronicnetcore.common
|            |---viewmodel    // ViewModels
|            |   |---AppViewModel.kt
|            |   |---MainViewModel.kt
|            |---AppComponent.kt    // Component which responsible for whole app
|            |---AppContext.kt    // definition for global application Context
|---main
|   |---kotlin
|   |   |---apptemplate.apptronicnetcore
|   |   |   |---binders
|   |   |   |   |---AppViewBinder.kt    // View binder to dusplay AppViewModel.kt
|   |   |   |   |---MainViewBinder.kt    // View binder to dusplay MainViewModel.kt
|   |   |   |---AndroidAppModule.kt    // Dependency module for providing Android-related dependencies
|   |   |   |---App.kt    // Application class
|   |   |   |---AppActivity.kt    // Application Activity
|   |   |   |---AppBinderFactory.kt    // ViewBinderFactory: creates ViewBinders for concrete ViewModel
|   |---res
|   |---AndroidManifest.xml
|---test
|   |---kotlin
|       |---apptemplate.apptronicnetcore
|           |---AppViewModelTest.kt    // Example of writing Unit test
|           |---MainViewModelTest.kt    // Example of writing Unit test
```

##### Prepare project

Go to ```app/build.gradle``` and change applicationId to your app application id. 
```groovy
android {
    ...
    defaultConfig {
        ...
        applicationId "apptemplate.apptronicnetcore" // Change this
```

Rename package ```apptemplate.apptronicnetcore``` to your app package name under source roots ```commonMain```, ```main``` and ```test```.

Rename package ```apptemplate.apptronicnetcore``` in ```AndroidManifest.xml```.

### Create project manually

First open Intellij Idea / Android Studio and start Kotlin Multiplatform project

##### Setup build.gradle

| **Dependency**              | **Version** |
|-----------------------------|-------------|
| apptronic.net/core-commons  | 0.8.6       |
| apptronic.net/core-android  | 0.8.10      |
| Kotlin                      | 1.4.10      |
| Coroutines                  | 1.3.9       |

Add kotlin plugin and apptronic.net maven repository:

```groovy
buildscript {
    ...
    dependencies {
        ...
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.10"
    }
}

allprojects {
    repositories {
        ...
        maven {
            url "https://maven.apptronic.net"
        }
    }
}
```

Open ```app/build.gradle```.

Add plugins:

```groovy
plugins {
    id 'kotlin-multiplatform'
    id "com.android.application"
    id "kotlin-android-extensions"
}
```

Library dependencies:

```groovy
kotlin {
    android("android")
    sourceSets {
        commonMain {
            dependencies {
                implementation kotlin('stdlib-common')
                implementation kotlin('reflect')
                implementation "net.apptronic.core:core-commons:0.7.0"
                implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9"
            }
        }
        androidMain {
            dependencies {
                implementation kotlin('stdlib')
                // required by to apptronic.net/core
                implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version"
                implementation "net.apptronic.core:core-android:$apptronic_net_core_version"

                // generic android dependencies
                implementation 'androidx.core:core-ktx:1.3.1'
                implementation 'androidx.appcompat:appcompat:1.2.0'
                implementation 'androidx.constraintlayout:constraintlayout:2.0.1'
                implementation "com.google.android.material:material:1.2.1"
                implementation 'androidx.recyclerview:recyclerview:1.1.0'              }
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

class AppComponent(context: Context) : BaseComponent(AppContext) {

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

class AppViewModel(parent: Context) : ViewModel(parent, EmptyViewModelContext) {

    val message = value("Hello from apptronic.net Core")

    fun onBackPressed(): Boolean {
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
            // this specifies common view factory to be used in all bindings
            binderFactory(AppBinderFactory)
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

app_view.xml

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


AppViewBinder.kt

```kotlin
// this class declares View layer for concrete ViewModel type

class AppViewBinder : AndroidView<AppViewModel>() {

    // baseic declaration of some layout to be used
    override var layoutResId: Int? = R.layout.app_view

    override fun onBindView(view: View, viewModel: AppViewModel) {
        with(view) {
            // lets bind content of TextView to data inside VideModel
            bindText(textView, viewModel.message)
            // do bindings
        }
    }

}
```

AppBinderFactory.kt

```kotlin
// this object is registry ob View to ViewModel bindings
// it was used on installation of Android plugin and will be used by detault in all navigators
// and for Activity bindings
// it is possible to override it for concrete Navigator biding if needed

val AppBinderFactory = viewBinderFactory {
    addBinding(::AppViewBinder)
}
```

MainActivity.kt

```kotlin
class MainActivity : AppCompatActivity() {

    // nothing needed to write here as all integration done by plugin
    // setContentView() also called by plugin
    // but do not forget to add it to AndroidManifest.xml

    // it is possible to made manual integration but not needed for most of cases
    // what may be needed it window initialization and other similar UI features

}
```

___

Read [Manual](manual.md) for complete guide to framework.