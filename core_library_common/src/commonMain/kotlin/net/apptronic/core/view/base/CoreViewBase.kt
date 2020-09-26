package net.apptronic.core.view.base

import net.apptronic.core.view.CoreView
import net.apptronic.core.view.CoreViewHolder
import net.apptronic.core.view.properties.LTR
import net.apptronic.core.view.properties.RTL

interface CoreViewBase : CoreViewHolder {

    val viewConfiguration: ViewConfiguration

    val CoreView.isLTR: Boolean
        get() {
            return this.viewConfiguration.layoutDirection == LTR
        }

    val CoreView.isRTL: Boolean
        get() {
            return this.viewConfiguration.layoutDirection == RTL
        }

}