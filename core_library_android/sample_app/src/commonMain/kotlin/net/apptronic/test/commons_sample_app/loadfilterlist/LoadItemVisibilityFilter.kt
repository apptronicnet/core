package net.apptronic.test.commons_sample_app.loadfilterlist

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.functions.not
import net.apptronic.core.mvvm.viewmodel.navigation.VisibilityFilter

class LoadItemVisibilityFilter : VisibilityFilter<LoadItemViewModel> {

    override fun isReadyToShow(viewModel: LoadItemViewModel): Entity<Boolean> {
        return viewModel.isLoading.not()
    }

}