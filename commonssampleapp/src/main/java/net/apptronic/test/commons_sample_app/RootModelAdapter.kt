package net.apptronic.test.commons_sample_app

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import net.apptronic.common.android.ui.components.fragment.FragmentViewModelAdapter
import net.apptronic.common.android.ui.viewmodel.ViewModel
import net.apptronic.test.commons_sample_app.fragments.NewInputScreenFragment
import net.apptronic.test.commons_sample_app.fragments.StartScreenFragment
import net.apptronic.test.commons_sample_app.fragments.YesNoSelectorFragment
import net.apptronic.test.commons_sample_app.models.NewInputScreenModel
import net.apptronic.test.commons_sample_app.models.StartScreenModel
import net.apptronic.test.commons_sample_app.models.YesNoSelectorViewModel

class RootModelAdapter(
    fragmentManager: FragmentManager,
    @IdRes containerViewId: Int
) : FragmentViewModelAdapter(fragmentManager, containerViewId) {

    override fun createFragment(viewModel: ViewModel): Fragment? {
        return when (viewModel) {
            is StartScreenModel -> StartScreenFragment()
            is NewInputScreenModel -> NewInputScreenFragment()
            is YesNoSelectorViewModel -> YesNoSelectorFragment()
            else -> null
        }
    }

}