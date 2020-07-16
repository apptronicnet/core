package net.apptronic.test.commons_sample_compat_app.data

import java.io.Serializable

data class UserData(
    val firstName: String,
    val lastName: String,
    val country: String,
    val city: String
) : Serializable