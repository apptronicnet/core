# apptronic.net / core

### Next generation framework for developing multi-platform applications

[Documentation page](https://apptronicnet.github.io/core/)

Demo app which demonstrates basics of **apptronic.net/core**:<br/>[https://github.com/apptronicnet/core_demo_android_app](https://github.com/apptronicnet/core_demo_android_app)

With this framework it possible to develop most code of application in Kotlin/Multiplatform:
whole architecture, app model and UI model (using MVVM pattern).

#### Why apptronic.net / core?

This framework designed after many years in mobile application development industry to improve performance and quality of development process.

### 1. Less development team for same results.
Instead of development whole application twice for Android and iOS platforms **apptronic.net/core** allows to develop all business and UI logic code once using Kotlin/Multiplatform.
 - code for both apps same meaning there is no more situations when apps on different platforms works differently
 - no more need two fully separate development teams for Android and iOS - single team will develop whole app
 - app UI developed using native platform, allowing usage any native platform features like gestures etc.
### 2. Less time-to-market with better quality and happy developers.
Framework have integrated out of the box modern and effective architecture approaches, making development of applications much faster. So, no more question "Quality or development performance?". Best code quality with modular architecture without no additional time cost.
 - Context-oriented and Component-based architecture: define Context (responsibility area) and implement how it works using simple or multiple Components
 - integrated powerful Dependency Injection framework with minimalistic API
 - integrated reactive programming framework for components with automated subscription management (forget about subscription leaks) and declarative programming
 - integration with Kotlin/Coroutines - forget about classic threading and all of it's issues
 - UI-Model control inversion: Model is the primary, UI serves. No more issues with UI state, now it just state-renderer.
 - MVVM/Navigation framework based on Components (ViewModel is sub-class of Component with some additional specific features)
 - extendable by plugins and extensions, allowing injection of additional features without rewriting framework code
### 3. Automated testing is no more pain.
Provides ability to cover anything by Unit, Integration and UI-Logic tests:
 - can be compiled to Java Bytecode - all tests runs on powerful Java testing frameworks: JUnit, Mockito etc.
 - allows isolation of app parts for unit testing or test whole app code as single module with mocked or real back-end
 - user interface defined as ViewModel tree with one root. Now it is possible to perform UI-Logic tests: simulate whole UI without it's rendering on mobile device for automated testing. For this tests backing model (database, cache, REST API) can be real or mocked.
 - no more needed to use real-device for testing: tests runs faster and can be set up on any CI as simple Gradle task

### Compatibility

Fully compatible with Kotlin/Multiplatform to develop common code for Android and iOS.
 
In the future it is possible to add support for Web and any other platforms, supported by Kotlin/Multiplatform itself.

### What is needed to do on platform side (Android, iOS)?

1. UI layout and ViewModel bindings.
2. Specific interfaces to work with system features like GPS, Permissions, Bluetooth, NFC, Fingerprint etc.

In most cases it is no more than 10-15% of all code.

### Current status of project:

 - ready for development multi-platform core for mobile apps
 - ready for use in Android projects
  
### iOS development

UI Binding library for iOS is under development

### How to start

To start project using framework follow to [Getting started guide](doc/getting_started.md)

Read [Manual](doc/manual.md) for complete guide to framework.

#### Contact info

For any questions you can contact via email: apptronic.net[at]gmail.com