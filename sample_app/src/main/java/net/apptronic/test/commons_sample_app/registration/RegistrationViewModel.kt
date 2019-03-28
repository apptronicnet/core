package net.apptronic.test.commons_sample_app.registration

import net.apptronic.core.component.entity.entities.setAs
import net.apptronic.core.component.entity.functions.variants.*
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.mvvm.viewmodel.extensions.forEachChangeAnyOf
import net.apptronic.test.commons_sample_app.app.ApplicationContext
import net.apptronic.test.commons_sample_app.app.Platform

class RegistrationViewModel(context: ViewModelContext) : ViewModel(context) {

    val email = value("")
    val password = value("")
    val passwordConfirmation = value("")

    private val httpClient = objects().get(ApplicationContext.HttpClientDescriptor)
    private val platform = objects().get(ApplicationContext.PlatformDescriptor)

    private val confirmedPassword = value<String?>().setAs(
        whenever(password isEqualsTo passwordConfirmation) then (password.toNullable() orElse nullValue())
    )

    val registerButtonEnabled =
        email.isNotEmpty() and password.isNotEmpty() and passwordConfirmation.isNotEmpty()

    val showPasswordsDoesNotMatchError = value(false)

    val registerButtonClickEvent = genericEvent()

    init {
        if (platform == Platform.Android) {
            //do 1
        }
        if (platform == Platform.iOS) {
            //do 2
        }
        forEachChangeAnyOf(password, passwordConfirmation) {
            showPasswordsDoesNotMatchError.set(false)
        }
        registerButtonClickEvent.subscribe {
            if (confirmedPassword.getOrNull() != null) {
                // TODO
            } else {
                showPasswordsDoesNotMatchError.set(true)
            }
        }
    }

}