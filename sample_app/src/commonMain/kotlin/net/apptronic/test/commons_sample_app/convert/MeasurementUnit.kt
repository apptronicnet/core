package net.apptronic.test.commons_sample_app.convert

enum class MeasurementUnit(
    val multiplier: Float,
    val unitName: String
) {

    Km(1.0f, "Kilometer"),
    Miles(1.609f, "Mile")

}