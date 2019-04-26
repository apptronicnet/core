package net.apptronic.test.commons_sample_app.convert

import android.view.View
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.screen_convert.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.ViewModelBinding
import net.apptronic.core.android.viewmodel.ViewToPredicateBinding
import net.apptronic.core.android.viewmodel.bindings.InputFieldBinding
import net.apptronic.core.android.viewmodel.bindings.TextBinding
import net.apptronic.core.component.entity.entities.Property
import net.apptronic.core.component.entity.functions.variants.map
import net.apptronic.core.component.entity.subscribe
import net.apptronic.test.commons_sample_app.R

class ConvertScreenView : AndroidView<ConvertScreenViewModel>() {

    init {
        layoutResId = R.layout.screen_convert
    }

    override fun onCreateBinding(
        view: View,
        viewModel: ConvertScreenViewModel
    ): ViewModelBinding<ConvertScreenViewModel> {
        return createBinding(view, viewModel) {
            with(view) {
                inputDistanceInKm.bindTo(InputFieldBinding(), viewModel.inputDistance)
                inputCostPerKmInUsd.bindTo(InputFieldBinding(), viewModel.inputCost)

                unitKM.bindTo(SelectorBinding(MeasurementUnit.Km), viewModel.unit)
                unitMiles.bindTo(SelectorBinding(MeasurementUnit.Miles), viewModel.unit)

                currencyUSD.bindTo(SelectorBinding(Currency.USD), viewModel.currency)
                currencyEUR.bindTo(SelectorBinding(Currency.EUR), viewModel.currency)
                currencyGBP.bindTo(SelectorBinding(Currency.GBP), viewModel.currency)

                costPerKm.bindTo(TextBinding(), viewModel.distanceCostText)
                totalCost.bindTo(TextBinding(), viewModel.costResult.map { it.toString() })
            }
        }
    }

    private class SelectorBinding<T>(val value: T) :
        ViewToPredicateBinding<View, T, Property<T>> {

        override fun performBinding(binding: ViewModelBinding<*>, view: View, target: Property<T>) {
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
            binding.doOnUnbind {
                view.setOnClickListener(null)
            }
        }

    }

}