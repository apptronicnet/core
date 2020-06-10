package net.apptronic.test.commons_sample_app.views

import android.view.View
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.screen_convert.view.*
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindText
import net.apptronic.core.android.viewmodel.bindings.bindTextInput
import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.component.entity.entities.Value
import net.apptronic.core.component.entity.functions.map
import net.apptronic.core.component.entity.functions.mapToString
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.convert.ConvertScreenViewModel
import net.apptronic.test.commons_sample_app.convert.Currency
import net.apptronic.test.commons_sample_app.convert.MeasurementUnit

class ConvertScreenViewBinder : ViewBinder<ConvertScreenViewModel>() {

    override var layoutResId: Int? = R.layout.screen_convert

    override fun onBindView(view: View, viewModel: ConvertScreenViewModel) {
        with(view) {
            bindTextInput(inputDistanceInKm, viewModel.inputDistance)
            bindTextInput(inputCostPerKmInUsd, viewModel.inputCost)

            add(SelectorBinding(unitKM, MeasurementUnit.Km, viewModel.unit))
            add(SelectorBinding(unitMiles, MeasurementUnit.Miles, viewModel.unit))

            add(SelectorBinding(currencyUSD, Currency.USD, viewModel.currency))
            add(SelectorBinding(currencyEUR, Currency.EUR, viewModel.currency))
            add(SelectorBinding(currencyGBP, Currency.GBP, viewModel.currency))

            bindText(costPerKm, viewModel.distanceCostText)
            bindText(totalCost, viewModel.costResult.mapToString())
        }
    }

    private class SelectorBinding<T>(
        private val view: View,
        private val value: T,
        private val target: Value<T>
    ) : Binding() {

        override fun onBind(viewModel: ViewModel, viewBinder: ViewBinder<*>) {
            target.map { it == value }.subscribe {
                if (it) {
                    view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.selection))
                } else {
                    view.background = null
                }
            }
            view.setOnClickListener {
                target.set(value)
            }
            onUnbind {
                view.setOnClickListener(null)
            }
        }

    }

}