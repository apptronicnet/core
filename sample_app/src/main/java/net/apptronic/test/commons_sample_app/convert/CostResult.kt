package net.apptronic.test.commons_sample_app.convert

data class CostResult(
    val unit: MeasurementUnit,
    val currency: Currency,
    val distance: Float?,
    val totalCost: Float?
) {

    override fun toString(): String {
        val distance = if (distance != null) "%.4f".format(distance) else null
        val totalCost = if (distance != null) "%.4f".format(totalCost) else null
        return if (totalCost != null && distance != null) {
            "Total cost: $totalCost ${currency.currencyName} per $distance ${unit.unitName}s"
        } else "-- Incorrect data --"
    }

}