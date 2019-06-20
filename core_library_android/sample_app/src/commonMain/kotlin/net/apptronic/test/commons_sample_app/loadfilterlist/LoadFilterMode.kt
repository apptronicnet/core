package net.apptronic.test.commons_sample_app.loadfilterlist

enum class LoadFilterMode(
    val title: String
) {

    Simple("Simple"),
    Random("Random"),
    RandomTakeFirst("Random but take only first"),
    RandomWithNotifyReady("Random and notify next when ready"),

}