package net.apptronic.test.commons_sample_app.registration

import net.apptronic.core.commons.navigation.injectNavigationRouter
import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.context.viewModelContext
import net.apptronic.core.component.entity.entities.setAs
import net.apptronic.core.component.entity.entities.subscribe
import net.apptronic.core.component.entity.functions.*
import net.apptronic.core.component.genericEvent
import net.apptronic.core.component.inject
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.mvvm.viewmodel.extensions.forEachChangeAnyOf
import net.apptronic.test.commons_sample_app.BackToLogin
import net.apptronic.test.commons_sample_app.app.HttpClientDescriptor
import net.apptronic.test.commons_sample_app.app.PlatformDefinition
import net.apptronic.test.commons_sample_app.app.PlatformDescriptor
import net.apptronic.test.commons_sample_app.login.RegistrationListener

fun Contextual.registrationViewModel(listener: RegistrationListener) =
    RegistrationViewModel(viewModelContext(), listener)

class RegistrationViewModel internal constructor(
    context: ViewModelContext, private val listener: RegistrationListener
) : ViewModel(context) {

    private val httpClient = inject(HttpClientDescriptor)
    private val platform = inject(PlatformDescriptor)
    private val router = injectNavigationRouter()

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
                listener.onRegistrationDone(email.get())
                router.sendCommands(BackToLogin())
            } else {
                showPasswordsDoesNotMatchError.set(true)
            }
        }
    }

}