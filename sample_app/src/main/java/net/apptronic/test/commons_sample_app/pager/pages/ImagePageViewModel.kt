package net.apptronic.test.commons_sample_app.pager.pages

import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.pager.NextPageNumberDescriptor
import kotlin.random.Random

private val IMAGE_VARIANTS =
    listOf(R.drawable.ic_list_item_1, R.drawable.ic_list_item_2, R.drawable.ic_list_item_3)
private val COLOR_VARIANTS =
    listOf(R.color.imageListItem1, R.color.imageListItem2, R.color.imageListItem3)

class ImagePageViewModel(context: ViewModelContext) : ViewModel(context) {

    private val pageNumber = getProvider().inject(NextPageNumberDescriptor)

    val number = value("Image $pageNumber")

    val imageRes = value(IMAGE_VARIANTS[Random.nextInt(IMAGE_VARIANTS.size)])
    val colorRes = value(COLOR_VARIANTS[Random.nextInt(COLOR_VARIANTS.size)])

    private val imageResToggle = toggle(imageRes, *IMAGE_VARIANTS.toTypedArray())
    private val colorResToggle = toggle(colorRes, *COLOR_VARIANTS.toTypedArray())

    fun onImageClick() {
        if (Random.nextBoolean()) {
            imageResToggle.toggle()
        } else {
            colorResToggle.toggle()
        }
    }

}