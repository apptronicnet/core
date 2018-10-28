package net.apptronic.common.android.ui.components.submodels

import net.apptronic.common.android.ui.viewmodel.ViewModel

class TextViewModel(parent: ViewModel) : ViewModel.SubModel(parent) {

    val text = value<String>()

    val textColor = value<Int>()

}