package net.apptronic.core.mvvm.viewmodel.container

import net.apptronic.core.component.entity.EntityValue
import net.apptronic.core.component.entity.entities.ComponentEntity
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelParent

abstract class Navigator<T>(viewModel: ViewModel) : ComponentEntity<T>(viewModel),
    EntityValue<T>, ViewModelParent