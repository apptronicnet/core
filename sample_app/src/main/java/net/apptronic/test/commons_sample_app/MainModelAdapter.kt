package net.apptronic.test.commons_sample_app

import android.view.ViewGroup
import net.apptronic.core.android.viewmodel.AndroidViewModelAdapter
import net.apptronic.test.commons_sample_app.login.LoginScreenView
import net.apptronic.test.commons_sample_app.registration.RegistrationScreenView

class MainModelAdapter(viewGroup: ViewGroup) : AndroidViewModelAdapter(viewGroup) {

    init {
        bindings {
            addBinding(::LoginScreenView)
            addBinding(::RegistrationScreenView)
        }
    }

}