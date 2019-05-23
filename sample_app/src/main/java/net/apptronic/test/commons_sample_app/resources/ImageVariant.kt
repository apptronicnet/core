package net.apptronic.test.commons_sample_app.resources

import net.apptronic.test.commons_sample_app.R

fun ImageVariant.getResourceId(): Int {
    return when (this) {
        ImageVariant.ListItem1 -> R.drawable.ic_list_item_1
        ImageVariant.ListItem2 -> R.drawable.ic_list_item_2
        ImageVariant.ListItem3 -> R.drawable.ic_list_item_3
    }
}