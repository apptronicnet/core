package net.apptronic.test.commons_sample_app

import net.apptronic.core.android.viewmodel.viewBinderFactory
import net.apptronic.test.commons_sample_app.views.*

val AppViewFactory = viewBinderFactory {
    addBinding(::MainActivityViewBinder)
    addBinding(::LoginScreenViewBinder)
    addBinding(::RegistrationScreenViewBinder)
    addBinding(::NavigationScreenViewBinder)
    addBinding(::ConvertScreenViewBinder)
    addBinding(::ListScreenViewBinder)
    addBinding(::ListItemTextViewBinder)
    addBinding(::ListItemImageViewBinder)
    addBinding(::PagerViewBinder)
    addBinding(::TextPageViewBinder)
    addBinding(::ImagePageViewBinder)
    addBinding(::ThrottleSampleViewBinder)
    addBinding(::LazyListViewBinder)
    addBinding(::LazyListItemViewBinder)
    addBinding(::StaticItemViewBinder)
    addBinding(::LoadFilterListViewBinder)
    addBinding(::LoadFilterListItemViewBinder)
    addBinding(::StackLoadingViewBinder)
    addBinding(::StackLoadingItemViewBinder)
    addBinding(::DynamicItemViewBinder)
    addBinding(::LazyListFilterViewBinder)
    addBinding(::StaticFilteredItemViewBinder)
}