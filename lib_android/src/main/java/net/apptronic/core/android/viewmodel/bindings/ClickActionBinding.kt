package net.apptronic.core.android.viewmodel.bindings

import android.view.View
import net.apptronic.core.android.viewmodel.ViewModelBinding
import net.apptronic.core.android.viewmodel.ViewToGenericActionBinding

class ClickActionBinding : ViewToGenericActionBinding<View>() {

    override fun onPerformBinding(binding: ViewModelBinding<*>, view: View, target: () -> Unit) {
        view.setOnClickListener {
            target.invoke()
        }
        binding.doOnUnbind {
            view.setOnClickListener(null)
        }
    }

}