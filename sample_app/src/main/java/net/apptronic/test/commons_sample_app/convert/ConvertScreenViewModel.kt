package net.apptronic.test.commons_sample_app.convert

import net.apptronic.core.component.entity.behavior.merge
import net.apptronic.core.component.entity.functions.variants.map
import net.apptronic.core.component.entity.functions.variants.mapOrNull
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext

class ConvertScreenViewModel(context: ViewModelContext) : ViewModel(context) {

    val inputDistance = value<String>("10")

    private val distanceValue = inputDistance.map {
        it.toFloatOrNull()
    }

    val inputCost = value<String>("1")

    private val costValue = inputCost.map {
        it.toFloatOrNull()
    }

    val unit = value(MeasurementUnit.Km)
    val currency = value(Currency.USD)

    private val distanceInUnit = merge(distanceValue, unit) { km, unit ->
        if (km != null) {
            km / unit.multiplier
        } else null
    }

    private val costPerUnitInCurrency = merge(costValue, unit, currency) { cost, unit, currency ->
        if (cost != null) {
            cost * unit.multiplier / currency.multiplier
        } else null
    }

    private val totalCost = merge(distanceInUnit, costPerUnitInCurrency) { distance, cost ->
        if (distance != null && cost != null) {
            distance * cost
        } else null
    }

    private val costInUnitTextValue = costPerUnitInCurrency.mapOrNull { "%.4f".format(it) }
    private val totalCostTextValue = totalCost.mapOrNull { "%.4f".format(it) }
    private val distanceTextValue = distanceInUnit.mapOrNull { "%.4f".format(it) }

    val distanceCostText =
        merge(unit, costInUnitTextValue, currency) { unit, cost, currency ->
            if (cost != null) {
                "Cost per 1 ${unit.unitName} is $cost ${currency.currencyName}"
            } else "-- Incorrect data --"
        }

    val totalCostText =
        merge(
            totalCostTextValue,
            currency,
            distanceTextValue,
            unit
        ) { total, currency, distance, unit ->
            if (total != null && distance != null) {
                "Total cost: $total ${currency.currencyName} per $distance ${unit.unitName}s"
            } else "-- Incorrect data --"
        }

}