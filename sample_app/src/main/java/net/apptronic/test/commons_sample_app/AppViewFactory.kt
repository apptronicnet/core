package net.apptronic.test.commons_sample_app

import net.apptronic.core.android.viewmodel.androidViewFactory
import net.apptronic.test.commons_sample_app.views.*

val AppViewFactory = androidViewFactory {
    addBinding(::MainActivityView)
    addBinding(::LoginScreenView)
    addBinding(::RegistrationScreenView)
    addBinding(::NavigationScreenView)
    addBinding(::ConvertScreenView)
    addBinding(::ListScreenView)
    addBinding(::ListItemTextView, R.layout.list_item_text)
    addBinding(::ListItemImageView, R.layout.list_item_image)
    addBinding(::PagerView)
    addBinding(::TextPageView)
    addBinding(::ImagePageView)
    addBinding(::DebounceSampleView)
}