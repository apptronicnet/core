package net.apptronic.core.android.compat

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import net.apptronic.core.context.Context
import net.apptronic.core.context.childContext

internal fun Activity.getParentContext(): Context {
    return (application as? CoreCompatContextHolder)?.componentContext
        ?: throw IllegalStateException(
            "Cannot find parent net.apptronic.core.component.context.Context. Application should implement ICoreCompatApplication"
        )
}

fun Fragment.getParentContext(): Context {
    return (parentFragment as? CoreCompatContextHolder)?.componentContext
        ?: (activity as? CoreCompatContextHolder)?.componentContext
        ?: (activity?.application as? CoreCompatContextHolder)?.componentContext
        ?: throw IllegalStateException(
            "Cannot find parent net.apptronic.core.component.context.Context. One the the following should be implemented:" +
                    "\nParent Fragment should implement ICoreCompatFragment" +
                    "\nParent Activity should implement ICoreCompatActivity" +
                    "\nApplication should implement ICoreCompatApplication"
        )
}

fun FragmentActivity.componentContext(builder: Context.() -> Unit) = lazy {
    ViewModelProvider(this, ContextHolderViewModelFactory {
        val parentContext = getParentContext()
        parentContext.childContext(builder)
    }).get(CoreCompatContextHolderViewModel::class.java).context
}

fun Fragment.componentContext(builder: Context.() -> Unit) = lazy {
    ViewModelProvider(this, ContextHolderViewModelFactory {
        val parentContext = getParentContext()
        parentContext.childContext(builder)
    }).get(CoreCompatContextHolderViewModel::class.java).context
}