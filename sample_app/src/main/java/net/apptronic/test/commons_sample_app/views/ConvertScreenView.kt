package net.apptronic.test.commons_sample_app.views

import android.view.View
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.screen_convert.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.bindings.asInputFor
import net.apptronic.core.android.viewmodel.bindings.setTextFrom
import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.component.entity.entities.Property
import net.apptronic.core.component.entity.functions.variants.map
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.convert.ConvertScreenViewModel
import net.apptronic.test.commons_sample_app.convert.Currency
import net.apptronic.test.commons_sample_app.convert.MeasurementUnit

class ConvertScreenView : AndroidView<ConvertScreenViewModel>() {

    init {
        layoutResId = R.layout.screen_convert
    }

    override fun onBindView(view: View, viewModel: ConvertScreenViewModel) {
        with(view) {
            +(inputDistanceInKm asInputFor viewModel.inputDistance)
            +(inputCostPerKmInUsd asInputFor viewModel.inputCost)

            +SelectorBinding(unitKM, MeasurementUnit.Km, viewModel.unit)
            +SelectorBinding(unitMiles, MeasurementUnit.Miles, viewModel.unit)

            +SelectorBinding(currencyUSD, Currency.USD, viewModel.currency)
            +SelectorBinding(currencyEUR, Currency.EUR, viewModel.currency)
            +SelectorBinding(currencyGBP, Currency.GBP, viewModel.currency)

            +(costPerKm setTextFrom viewModel.distanceCostText)
            +(totalCost setTextFrom viewModel.costResult.map { it.toString() })
        }
    }

    private class SelectorBinding<T>(
        private val view: View,
        private val value: T,
        private val target: Property<T>
    ) : Binding() {

        override fun onBind(viewModel: ViewModel, androidView: AndroidView<*>) {
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