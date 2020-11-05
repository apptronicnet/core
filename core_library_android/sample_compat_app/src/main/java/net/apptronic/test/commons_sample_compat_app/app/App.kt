package net.apptronic.test.commons_sample_compat_app.app

import android.app.Application
import net.apptronic.core.android.compat.ICoreCompatApplication
import net.apptronic.core.context.Context
import net.apptronic.core.context.coreContext

class App : Application(), ICoreCompatApplication {

    override val componentContext: Context = coreContext()

}