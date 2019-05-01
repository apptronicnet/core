package net.apptronic.test.commons_sample_app

import net.apptronic.core.android.viewmodel.viewBindingFactory
import net.apptronic.test.commons_sample_app.convert.ConvertScreenView
import net.apptronic.test.commons_sample_app.list.ListItemImageView
import net.apptronic.test.commons_sample_app.list.ListItemTextView
import net.apptronic.test.commons_sample_app.list.ListScreenView
import net.apptronic.test.commons_sample_app.login.LoginScreenView
import net.apptronic.test.commons_sample_app.navigation.NavigationScreenView
import net.apptronic.test.commons_sample_app.pager.PagerView
import net.apptronic.test.commons_sample_app.pager.pages.ImagePageView
import net.apptronic.test.commons_sample_app.pager.pages.TextPageView
import net.apptronic.test.commons_sample_app.registration.RegistrationScreenView

val MainModelFactory = viewBindingFactory {
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
}