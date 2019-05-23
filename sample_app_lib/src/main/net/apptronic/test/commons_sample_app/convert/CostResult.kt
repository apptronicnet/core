package net.apptronic.test.commons_sample_app.convert

data class CostResult(
    val unit: MeasurementUnit,
    val currency: Currency,
    val distance: Float?,
    val totalCost: Float?
) {

    override fun toString(): String {
        val distance = if (distance != null) distance.toString() else null
        val totalCost = if (distance != null) distance.toString() else null
        return if (totalCost != null && distance != null) {
            "Total cost: $totalCost ${currency.currencyName} per $distance ${unit.unitName}s"
        } else "-- Incorrect data --"
    }

}