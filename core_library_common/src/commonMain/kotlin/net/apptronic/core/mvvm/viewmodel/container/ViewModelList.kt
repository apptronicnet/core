package net.apptronic.core.mvvm.viewmodel.container

import net.apptronic.core.mvvm.viewmodel.ViewModel

class ViewModelList(
        val all: List<ViewModel>,
        val visible: List<ViewModel>
)