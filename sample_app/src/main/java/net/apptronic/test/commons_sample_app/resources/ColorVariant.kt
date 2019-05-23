package net.apptronic.test.commons_sample_app.resources

import net.apptronic.test.commons_sample_app.R

fun ColorVariant.getResourceId(): Int {
    return when (this) {
        ColorVariant.ListItem1 -> R.color.imageListItem1
        ColorVariant.ListItem2 -> R.color.imageListItem2
        ColorVariant.ListItem3 -> R.color.imageListItem3
    }
}