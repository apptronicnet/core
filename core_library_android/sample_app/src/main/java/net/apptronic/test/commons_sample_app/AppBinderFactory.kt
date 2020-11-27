package net.apptronic.test.commons_sample_app

import net.apptronic.core.android.viewmodel.viewBinderAdapter
import net.apptronic.test.commons_sample_app.binders.*
import net.apptronic.test.commons_sample_app.binders.tabs.TabListItemViewBinder
import net.apptronic.test.commons_sample_app.binders.tabs.TabPageViewBinder
import net.apptronic.test.commons_sample_app.binders.tabs.TabsViewBinder

val AppBinderFactory = viewBinderAdapter {
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
    add(::BottomSheetViewBinder)
    add(::VisibilityDemoViewBinder)
    add(::AnimationDemoViewBinder)
    add(::ViewTransitionDemoViewBinder)
    add(::TabListItemViewBinder)
    add(::TabPageViewBinder)
    add(::TabsViewBinder)
}