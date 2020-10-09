package net.apptronic.test.commons_sample_compat_app

import android.content.Intent
import android.os.Bundle
import net.apptronic.test.commons_sample_compat_app.about.AboutActivity
import net.apptronic.test.commons_sample_compat_app.data.UserData
import net.apptronic.test.commons_sample_compat_app.fragments.dialog.SampleDialog
import net.apptronic.test.commons_sample_compat_app.fragments.enterdata.EnterDataFragment
import net.apptronic.test.commons_sample_compat_app.fragments.showdata.KEY_USER_DATA
import net.apptronic.test.commons_sample_compat_app.fragments.showdata.ShowDataFragment

class RouterImpl(private val mainActivity: MainActivity) : Router {

    override fun openAbout() {
        mainActivity.startActivity(Intent(mainActivity, AboutActivity::class.java))
    }

    override fun openDialog() {
        mainActivity.supportFragmentManager.beginTransaction()
            .add(SampleDialog(), null)
            .commit()
    }

    override fun goToEnterData() {
        mainActivity.replaceFragmentWithAddToBackStack(EnterDataFragment())
    }

    override fun goToShowUserData(data: UserData) {
        mainActivity.replaceFragmentWithAddToBackStack(ShowDataFragment().apply {
            arguments = Bundle().apply {
                putSerializable(KEY_USER_DATA, data)
            }
        })
    }

}