package net.apptronic.core.android.viewmodel.listadapters

import android.content.Context
import net.apptronic.core.viewmodel.IViewModel

class TitleProvider(
    private val context: Context,
    private val titleAdapter: TitleAdapter
) {

    fun getItemTitle(viewModel: IViewModel, position: Int): String {
        return titleAdapter.getItemTitle(context, viewModel, position) ?: ""
    }

}

/**
 * Provides titles for pages in [ViewPagerAdapter]
 */
interface TitleAdapter {

    fun getItemTitle(context: Context, viewModel: IViewModel, position: Int): String?

}