package net.apptronic.test.commons_sample_app.registration

import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.component.entity.entities.setAs
import net.apptronic.core.component.entity.functions.variants.*
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.mvvm.viewmodel.extensions.forEachChangeAnyOf
import net.apptronic.test.commons_sample_app.app.ApplicationContext
import net.apptronic.test.commons_sample_app.app.PlatformDefinition

class RegistrationViewModel(context: ViewModelContext) : ViewModel(context) {

    private val httpClient = getProvider().inject(ApplicationContext.HttpClientDescriptor)
    private val platform = getProvider().inject(ApplicationContext.PlatformDescriptor)
    private val router =
        getProvider().inject(RegistrationViewModelContext.RegistrationRouterDescriptor)

    val email = value("")
    val password = value("")
    val passwordConfirmation = value("")

    private val confirmedPassword = value<String?>().setAs(
        whenever(password isEqualsTo passwordConfirmation) then (password.toNullable() orElse nullValue())
    )

    val registerButtonEnabled =
        email.isNotEmpty() and password.isNotEmpty() and passwordConfirmation.isNotEmpty()

    val showPasswordsDoesNotMatchError = value(false)

    val registerButtonClickEvent = genericEvent()

    init {
        if (platform == PlatformDefinition.Android) {
            //do 1
        }
        if (platform == PlatformDefinition.iOS) {
            //do 2
        }
        forEachChangeAnyOf(password, passwordConfirmation) {
            showPasswordsDoesNotMatchError.set(false)
        }
        registerButtonClickEvent.subscribe {
            if (confirmedPassword.getOrNull() != null) {
                router.backToLogin(email.get())
            } else {
                showPasswordsDoesNotMatchError.set(true)
            }
        }
    }

}