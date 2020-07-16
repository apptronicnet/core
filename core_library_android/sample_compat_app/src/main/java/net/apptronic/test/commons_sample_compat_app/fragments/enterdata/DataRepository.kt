package net.apptronic.test.commons_sample_compat_app.fragments.enterdata

import net.apptronic.test.commons_sample_compat_app.data.UserData

class DataRepository {

    var firstName = ""
    var lastName = ""
    var country = ""
    var city = ""

    fun getData(): UserData {
        return UserData(
            firstName = this.firstName,
            lastName = this.lastName,
            country = this.country,
            city = this.city
        )
    }

}