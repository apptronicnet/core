package net.apptronic.test.commons_sample_app

import net.apptronic.core.android.viewmodel.viewBindingFactory
import net.apptronic.test.commons_sample_app.convert.ConvertScreenView
import net.apptronic.test.commons_sample_app.list.ListItemImageView
import net.apptronic.test.commons_sample_app.list.ListItemTextView
import net.apptronic.test.commons_sample_app.list.ListScreenView
import net.apptronic.test.commons_sample_app.login.LoginScreenView
import net.apptronic.test.commons_sample_app.navigation.NavigationScreenView
import net.apptronic.test.commons_sample_app.registration.RegistrationScreenView

val MainModelFactory = viewBindingFactory {
    addBinding(LoginScreenView())
    addBinding(RegistrationScreenView())
    addBinding(NavigationScreenView())
    addBinding(ConvertScreenView())
    addBinding(ListScreenView())
    addBinding(ListItemTextView())
    addBinding(ListItemImageView())
}