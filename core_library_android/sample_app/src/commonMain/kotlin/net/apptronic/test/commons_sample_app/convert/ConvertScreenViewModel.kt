package net.apptronic.test.commons_sample_app.convert

import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.context.viewModelContext
import net.apptronic.core.component.entity.functions.map
import net.apptronic.core.component.entity.functions.mapOrNull
import net.apptronic.core.component.entity.functions.merge
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext

fun Contextual.convertScreenViewModel() = ConvertScreenViewModel(viewModelContext())

class ConvertScreenViewModel internal constructor(context: ViewModelContext) : ViewModel(context) {

    /**
     * Input field with user text of distance
     */
    val inputDistance = value<String>("10")

    private val distanceValue = inputDistance.map {
        it.toFloatOrNull()
    }

    /**
     * Input field with user text of cost
     */
    val inputCost = value<String>("1")

    private val costValue = inputCost.map {
        it.toFloatOrNull()
    }

    /**
     * Selector for unit
     */
    val unit = value(MeasurementUnit.Km)
    /**
     * Selector for currency
     */
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

    private val costInUnitTextValue = costPerUnitInCurrency.mapOrNull { it.toString() }
//    private val totalCostTextValue = totalCost.mapOrNull { "%.4f".format(it) }
//    private val distanceTextValue = distanceInUnit.mapOrNull { "%.4f".format(it) }

    val costResult =
        merge(costPerUnitInCurrency, totalCost, currency, unit) { cost, distance, currency, unit ->
            CostResult(
                unit = unit,
                currency = currency,
                totalCost = cost,
                distance = distance
            )
        }

    val distanceCostText =
        merge(unit, costInUnitTextValue, currency) { unit, cost, currency ->
            if (cost != null) {
                "Cost per 1 ${unit.unitName} is $cost ${currency.currencyName}"
            } else "-- Incorrect data --"
        }

//    val totalCostText =
//        merge(
//            totalCostTextValue,
//            currency,
//            distanceTextValue,
//            unit
//        ) { total, currency, distance, unit ->
//            if (total != null && distance != null) {
//                "Total cost: $total ${currency.currencyName} per $distance ${unit.unitName}s"
//            } else "-- Incorrect data --"
//        }

}