package net.apptronic.test.commons_sample_compat_app

import net.apptronic.test.commons_sample_compat_app.data.UserData

interface Router {

    fun openAbout()

    fun openDialog()

    fun goToEnterData()

    fun goToShowUserData(data: UserData)

}