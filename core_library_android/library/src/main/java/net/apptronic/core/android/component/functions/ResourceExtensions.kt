package net.apptronic.core.android.component.functions

import android.content.Context
import android.content.res.Resources
import android.os.Build
import androidx.core.content.ContextCompat
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.functions.map

fun Entity<Int>.resourceToColor(resources: Resources): Entity<Int> {
    return map { colorResId ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            resources.getColor(colorResId, null)
        } else {
            resources.getColor(colorResId)
        }
    }
}

fun Entity<Int>.resourceToColor(context: Context): Entity<Int> {
    return map { colorResId ->
        ContextCompat.getColor(context, colorResId)
    }
}

fun Entity<Int>.resourceToString(resources: Resources): Entity<String> {
    return map { stringResId ->
        resources.getString(stringResId)
    }
}

fun Entity<Int>.resourceToString(context: Context): Entity<String> {
    return map { stringResId ->
        context.getString(stringResId)
    }
}