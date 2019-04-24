package net.apptronic.test.commons_sample_app.convert

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.screen_convert.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.bindTo
import net.apptronic.core.component.entity.entities.Property
import net.apptronic.core.component.entity.functions.variants.map
import net.apptronic.core.component.entity.subscribe
import net.apptronic.test.commons_sample_app.R

class ConvertScreenView(viewModel: ConvertScreenViewModel) :
    AndroidView<ConvertScreenViewModel>(viewModel) {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.screen_convert, container, false)
    }

    override fun onBindView() {
        with(getView()) {
            inputDistanceInKm.bindTo(viewModel.inputDistance)
            inputCostPerKmInUsd.bindTo(viewModel.inputCost)

            unitKM.bindAsSelector(viewModel.unit, MeasurementUnit.Km)
            unitMiles.bindAsSelector(viewModel.unit, MeasurementUnit.Miles)

            currencyUSD.bindAsSelector(viewModel.currency, Currency.USD)
            currencyEUR.bindAsSelector(viewModel.currency, Currency.EUR)
            currencyGBP.bindAsSelector(viewModel.currency, Currency.GBP)

            costPerKm.bindTo(viewModel.distanceCostText)
            totalCost.bindTo(viewModel.totalCostText)
        }
    }

    private fun <T> View.bindAsSelector(property: Property<T>, value: T) {
        setOnClickListener {
            property.set(value)
        }
        property.map { it == value }.subscribe {
            if (it) {
                setBackgroundColor(ContextCompat.getColor(context, R.color.selection))
            } else {
                background = null
            }
        }
    }

}