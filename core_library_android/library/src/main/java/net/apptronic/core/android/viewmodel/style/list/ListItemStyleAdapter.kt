package net.apptronic.core.android.viewmodel.style.list

import android.view.View
import net.apptronic.core.mvvm.viewmodel.ViewModel

interface ListItemStyleAdapter {

    fun applyViewStyle(view: View, position: Int, list: List<ViewModel>)

}