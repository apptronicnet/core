package net.apptronic.core.android.viewmodel.bindings.navigation

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.mvvm.viewmodel.navigation.ListNavigator
import net.apptronic.core.mvvm.viewmodel.navigation.StaticListNavigator
import net.apptronic.core.mvvm.viewmodel.navigation.models.IListNavigationModel
import net.apptronic.core.mvvm.viewmodel.navigation.models.ISelectorNavigationModel
import net.apptronic.core.mvvm.viewmodel.navigation.models.IStackNavigationModel

/**
 * Default binding for [IStackNavigationModel]
 */
fun BindingContainer.bindNavigator(
    container: ViewGroup,
    navigator: IStackNavigationModel
) {
    bindNavigator(container, navigator, SingleViewMode())
}

/**
 * Default binding for [ISelectorNavigationModel]
 */
fun BindingContainer.bindNavigator(
    container: ViewGroup,
    navigator: ISelectorNavigationModel
) {
    bindNavigator(container, navigator, SingleViewMode())
}

/**
 * Default binding for [IListNavigationModel] with [RecyclerView]
 */
fun BindingContainer.bindNavigator(
    container: RecyclerView,
    navigator: IListNavigationModel<*, *, *>
) {
    bindNavigator(container, navigator, ViewListMode())
}

/**
 * Default binding for [StaticListNavigator] and [ViewPager]
 */
fun BindingContainer.bindNavigator(
    container: ViewPager,
    navigator: StaticListNavigator<*>
) {
    bindNavigator(container, navigator, ViewPagerMode())
}


/**
 * Default binding for [IListNavigationModel] and [ViewPager2]
 */
fun BindingContainer.bindNavigator(
    container: ViewPager2,
    navigator: ListNavigator<*, *, *>,
) {
    bindNavigator(container, navigator, ViewPager2Mode())
}