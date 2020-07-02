package net.apptronic.test.commons_sample_app.views

import android.graphics.Color
import android.view.View
import kotlinx.android.synthetic.main.navigation_item.view.*
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindText
import net.apptronic.core.base.observable.subscribe
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.staknavigation.StackItemColor
import net.apptronic.test.commons_sample_app.staknavigation.StackItemViewModel

class StackNavigationItemViewBinder : ViewBinder<StackItemViewModel>() {

    override var layoutResId: Int? = R.layout.navigation_item

    override fun onBindView(view: View, viewModel: StackItemViewModel) {
        with(view) {
            bindText(text, viewModel.text)
            viewModel.backgroundColor.subscribe {
                val color = when (it) {
                    StackItemColor.Gray -> Color.GRAY
                    StackItemColor.Red -> Color.RED
                    StackItemColor.Green -> Color.GREEN
                    StackItemColor.Blue -> Color.BLUE
                }
                view.setBackgroundColor(color)
            }
        }
    }

}