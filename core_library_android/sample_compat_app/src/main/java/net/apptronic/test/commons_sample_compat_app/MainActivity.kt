package net.apptronic.test.commons_sample_compat_app

import android.os.Bundle
import androidx.fragment.app.Fragment
import net.apptronic.core.android.compat.ViewBinderActivity
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.component.context.Context
import net.apptronic.test.commons_sample_compat_app.fragments.welcome.WelcomeFragment

class MainActivity : ViewBinderActivity<MainViewModel>() {

    override fun buildViewModel(parent: Context): MainViewModel = parent.mainViewModel()

    override fun buildViewBinder(): ViewBinder<MainViewModel> = MainViewBinder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, WelcomeFragment())
                .commit()
        }
    }

    override fun onViewModelAttached(savedInstanceState: Bundle?) {
        super.onViewModelAttached(savedInstanceState)
        componentContext.dependencyDispatcher.addInstance<Router>(RouterImpl(this))
    }

    fun replaceFragmentWithAddToBackStack(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }

}