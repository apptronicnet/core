package net.apptronic.test.commons_sample_app.convert

enum class Currency(
    val multiplier: Float,
    val currencyName: String
) {

    USD(1.0f, "USD"),
    EUR(1.12f, "Euro"),
    GBP(1.29f, "Pound")

}