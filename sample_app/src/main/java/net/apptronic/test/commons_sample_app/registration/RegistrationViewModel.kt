package net.apptronic.test.commons_sample_app.registration

import net.apptronic.core.component.entity.entities.setAs
import net.apptronic.core.component.entity.functions.variants.*
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.mvvm.viewmodel.extensions.forEachChangeAnyOf

class RegistrationViewModel(context: ViewModelContext) : ViewModel(context) {

    val email = value("")
    val password = value("")
    val passwordConfirmation = value("")

    private val confirmedPassword = value<String?>().setAs(
        whenever(password isEqualsTo passwordConfirmation) then (password.toNullable() orElse nullValue())
    )

    val registerButtonEnabled =
        email.isNoEmpty() and password.isNoEmpty() and passwordConfirmation.isNoEmpty()

    val showPasswordsDoesNotMatchError = value(false)

    val registerButtonClickEvent = genericEvent()

    init {
        forEachChangeAnyOf(password, passwordConfirmation) {
            showPasswordsDoesNotMatchError.set(false)
        }
        registerButtonClickEvent.subscribe {
            if (confirmedPassword.getOrNull() != null) {

            } else {
                showPasswordsDoesNotMatchError.set(true)
            }
        }
    }

}