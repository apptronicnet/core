package net.apptronic.core.android.component.functions

import android.content.Context
import android.content.res.Resources
import android.os.Build
import androidx.core.content.ContextCompat
import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.functions.variants.map

fun Predicate<Int>.resourceToColor(resources: Resources): Predicate<Int> {
    return map { colorResId ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            resources.getColor(colorResId, null)
        } else {
            resources.getColor(colorResId)
        }
    }
}

fun Predicate<Int>.resourceToColor(context: Context): Predicate<Int> {
    return map { colorResId ->
        ContextCompat.getColor(context, colorResId)
    }
}

fun Predicate<Int>.resourceToString(resources: Resources): Predicate<String> {
    return map { stringResId ->
        resources.getString(stringResId)
    }
}

fun Predicate<Int>.resourceToString(context: Context): Predicate<String> {
    return map { stringResId ->
        context.getString(stringResId)
    }
}