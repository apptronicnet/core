package net.apptronic.test.commons_sample_app.views

import android.graphics.Color
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.navigation_item.view.*
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindSwitch
import net.apptronic.core.android.viewmodel.bindings.bindText
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
                stackPage.setBackgroundColor(color)
            }
            bindSwitch(allowBackNavigation, viewModel.isBackNavigationAllowed)
            viewModel.onGoBack.subscribe {
                Toast.makeText(context, "Gesture back", Toast.LENGTH_SHORT).show()
            }
            viewModel.onCantGoBack.subscribe {
                Toast.makeText(context, "No-no-no!", Toast.LENGTH_SHORT).show()
            }
        }
    }

}