package net.apptronic.test.commons_sample_app

import net.apptronic.core.android.viewmodel.viewBinderFactory
import net.apptronic.test.commons_sample_app.views.*

val AppBinderFactory = viewBinderFactory {
    add(::MainActivityViewBinder)
    add(::LoginScreenViewBinder)
    add(::RegistrationScreenViewBinder)
    add(::NavigationScreenViewBinder)
    add(::ConvertScreenViewBinder)
    add(::ListScreenViewBinder)
    add(::ListItemTextViewBinder)
    add(::ListItemImageViewBinder)
    add(::PagerViewBinder)
    add(::TextPageViewBinder)
    add(::ImagePageViewBinder)
    add(::ThrottleSampleViewBinder)
    add(::LazyListViewBinder)
    add(::LazyListItemViewBinder)
    add(::StaticItemViewBinder)
    add(::LoadFilterListViewBinder)
    add(::LoadFilterListItemViewBinder)
    add(::StackLoadingViewBinder)
    add(::StackLoadingItemViewBinder)
    add(::DynamicItemViewBinder)
    add(::LazyListFilterViewBinder)
    add(::StaticFilteredItemViewBinder)
    add(::GestureNavigationViewBinder)
    add(::StackNavigationItemViewBinder)
    add(::PrevNextNavigationViewBinder)
}