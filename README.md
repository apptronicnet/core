# apptronic.net / core
##### Multiplatform Kotlin framework for mobile apps

Current status of project: under development

For any questions you can contact via email: apptronic.net[at]gmail.com

##### What's done:
 * Kotlin Multiplatform framework for building mobile apps shared code
   - Architecture library
   - Reactive library
   - MVVM/Navigation library
   - Dependency Injection library
   - Plugin extensions
   - Integration with Kotlin Coroutines
 * Android MVVM/Binding library for use with shared code
   - Binding/Navigation framework for use with core framework
   - Framework Plugins for easy integration
   
How to start:

build.gradle

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

App.kt

    val AppContext = coreContext {
        installViewFactoryPlugin(AppViewFactory)
    }
    
    class AndroidApplication : Application() {
    
        val appComponent by lazy {
            AppComponent(appContext)
        }
    
    }
    
    fun Context.getApplicationComponent(): ApplicationComponent {
        return (applicationContext as AndroidApplication).appComponent
    }

AppComponent.kt (shared code)

    class AppComponent(context: Context) : BaseComponent(context) {
    
        val appUI: ViewModelDispatcher<ApplicationScreenViewModel> = viewModelDispatcher {
            AppViewModel(it)
        }
                
    }

MainActivity.kt

    class MainActivity : AppCompatActivity() {
    
        private val uiContainer by lazy {
            activityContainer(
                getApplicationComponent().appUI
            )
        }
    
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            uiContainer.onActivityCreate()
        }
    
        override fun onStart() {
            super.onStart()
            uiContainer.onActivityStart()
        }
    
        override fun onResume() {
            super.onResume()
            uiContainer.onActivityResume()
        }
    
        override fun onPause() {
            super.onPause()
            uiContainer.onActivityPause()
        }
    
        override fun onStop() {
            super.onStop()
            uiContainer.onActivityStop()
        }
    
        override fun onBackPressed() {
            getApplicationComponent().appUI.getViewModel().onBackPressed {
                finish()
            }
        }
    
        override fun onDestroy() {
            super.onDestroy()
            uiContainer.onActivityDestroy()
        }
    
    }

AppViewModel.kt (shared code)

    class ApplicationScreenViewModel(parent: Context) : ViewModel(parent, EMPTY_VIEW_MODEL_CONTEXT) {
    
        // implementation
    
    }

AppView.kt

    class AppView : AndroidView<AppViewModel>() {
    
        override var layoutResId: Int? = R.layout.app_view
    
        override fun onBindView(view: View, viewModel: ApplicationScreenViewModel) {
            with(view) {
                // do bindings
            }
        }
    
    }

AppViewFactory.kt

    val AppViewFactory = androidViewFactory {
        addBinding(::AppView)
    }