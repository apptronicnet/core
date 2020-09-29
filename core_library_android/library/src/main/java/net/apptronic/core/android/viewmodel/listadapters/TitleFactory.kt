package net.apptronic.core.android.viewmodel.listadapters

import android.content.Context
import net.apptronic.core.mvvm.viewmodel.IViewModel

class TitleProvider(
    private val context: Context,
    private val titleFactory: TitleFactory
) {

    fun getItemTitle(viewModel: IViewModel, position: Int): String {
        return titleFactory.getItemTitle(context, viewModel, position) ?: ""
    }

}

interface TitleFactory {

    fun getItemTitle(context: Context, viewModel: IViewModel, position: Int): String?

}