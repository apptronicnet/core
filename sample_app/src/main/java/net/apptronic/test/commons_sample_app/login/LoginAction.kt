package net.apptronic.test.commons_sample_app.login

import net.apptronic.core.component.process.BackgroundAction

object LoginAction : BackgroundAction<LoginRequest, LoginResult> {

    override fun execute(request: LoginRequest): LoginResult {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}


data class LoginRequest(
    val login: String,
    val password: String
)


data class LoginResult(
    val success: Boolean
)